package com.rafengimprove.currency.currencyrate.service.impl

import com.rafengimprove.currency.currencyrate.model.dto.*
import com.rafengimprove.currency.currencyrate.model.entity.ClientStatsEntity
import com.rafengimprove.currency.currencyrate.model.entity.toDto
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType
import com.rafengimprove.currency.currencyrate.model.type.OperationType
import com.rafengimprove.currency.currencyrate.model.type.OperationType.BUY
import com.rafengimprove.currency.currencyrate.model.type.OperationType.SELL
import com.rafengimprove.currency.currencyrate.repository.ClientStatsRepository
import com.rafengimprove.currency.currencyrate.service.ClientService
import com.rafengimprove.currency.currencyrate.service.ClientStatsService
import org.springframework.stereotype.Service
import java.util.*

@Service
class ClientStatsServiceImpl(
    val clientStatsRepository: ClientStatsRepository,
    val clientService: ClientService
) : ClientStatsService {

    override fun save(clientStatsDto: ClientStatsDto): ClientStatsDto {
        return clientStatsRepository.save(clientStatsDto.toEntity()).toDto()
    }

    private fun getClientStatsBy(
        clientId: Long,
        currencyType: CurrencyType,
        operationType: OperationType,
    ): Optional<ClientStatsEntity> {
        return clientStatsRepository.findByCurrencyTypeAndOperationTypeAndClientEntity_Id(
            currencyType,
            operationType,
            clientId,
        )
    }

    override fun getClientStatsBy(
        clientId: Long,
        currencyType: CurrencyType,
        operationType: OperationType,
        converter: (ClientStatsEntity) -> ClientStatsDto?
    ): ClientStatsDto? {
        return clientStatsRepository.findByCurrencyTypeAndOperationTypeAndClientEntity_Id(
            currencyType,
            operationType,
            clientId
        ).takeIf { it.isPresent }?.let { converter(it.get()) }

    }

    override fun editClientStatsBy(
        clientStatsEntity: ClientStatsEntity,
        amount: Double,
        modifier: (ClientStatsEntity) -> ClientStatsEntity
    ): ClientStatsDto? {
        return run { modifier(clientStatsEntity) }.let { clientStatsRepository.save(it) }
            .toDto()   //clientStatsRepository.save(modifier(clientStatsEntity)).toDto()
    }

    // Rates
    // operation_type           give                   receive             giveAmount   receiveAmount
    //      SEL:                USD             ->      RUB                  1                100
    //      BUY:                RUB             ->      USD                  100              1
    //      BUY:                USD             ->      EUR                  1                0.9
    //      SEL:                EUR             ->      USD                  1                1.1

    // exchange_operations
    // operation_type           give                   receive             giveAmount   receiveAmount
    //      SEL:                USD             ->      RUB                  100                10000
    //      BUY:                RUB             ->      USD                  10000              100

    // client_stat_info
    //      BUY:                RUB             ->      USD                  100000000          100     client_id
    //      BUY:                RUB             ->      USD                  100234             100     client_id
    //      BUY:                USD             ->      EUR                  1003456            97      client_id


    override fun modifyClientStats(
        exchangeOperation: ExchangeDataDto,
    ) {

        when (exchangeOperation.operationType) {
            SELL -> {
                ClientStatsUpdater(
                    clientService.findById(exchangeOperation.clientId),
                    exchangeOperation.toCurrencyType,
                    exchangeOperation.operationType,
                    exchangeOperation.giveAmount
                )
            }

            BUY -> {
                ClientStatsUpdater(
                    clientService.findById(exchangeOperation.clientId),
                    exchangeOperation.toCurrencyType,
                    exchangeOperation.operationType,
                    exchangeOperation.receiveAmount
                )
            }
        }.update {
            getClientStatsBy(
                it.oldClientDto.id!!,
                it.currencyType,
                it.operationType
            )
        }.let { clientStatsRepository.save(it) }
    }
}

class ClientStatsUpdater(
    val oldClientDto: ClientDto,
    val currencyType: CurrencyType,
    val operationType: OperationType,
    private val amount: Double
) {
    fun update(clientStatsLoader: (ClientStatsUpdater) -> Optional<ClientStatsEntity>): ClientStatsEntity {
        return clientStatsLoader(this)
            .map { it.total = it.total.plus(amount); it }
            .orElse(
                ClientStatsEntity(
                    currencyType = currencyType,
                    operationType = operationType,
                    total = amount
                ).apply { this.clientEntity = oldClientDto.toEntity() })
    }
}