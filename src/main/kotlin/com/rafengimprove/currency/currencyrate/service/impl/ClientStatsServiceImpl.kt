package com.rafengimprove.currency.currencyrate.service.impl

import com.rafengimprove.currency.currencyrate.model.dto.*
import com.rafengimprove.currency.currencyrate.model.entity.ClientStatsEntity
import com.rafengimprove.currency.currencyrate.model.entity.toDto
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType
import com.rafengimprove.currency.currencyrate.model.type.OperationType
import com.rafengimprove.currency.currencyrate.model.type.OperationType.BUY
import com.rafengimprove.currency.currencyrate.model.type.OperationType.SELL
import com.rafengimprove.currency.currencyrate.repository.ClientStatsRepository
import com.rafengimprove.currency.currencyrate.service.ClientStatsService
import org.springframework.stereotype.Service
import java.util.*

@Service
class ClientStatsServiceImpl(
    val clientStatsRepository: ClientStatsRepository
) : ClientStatsService {
    override fun save(clientStatsDto: ClientStatsDto): ClientStatsDto {
        return clientStatsRepository.save(clientStatsDto.toEntity()).toDto()
    }

    private fun getClientStatsBy(
        clientId: Long,
        currencyType: CurrencyType,
        operationType: OperationType,
    ): Optional<ClientStatsEntity> {
        return clientStatsRepository.findByCurrencyTypeAndCurrencyDirectionTypeAndClientEntity_Id(
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
        return clientStatsRepository.findByCurrencyTypeAndCurrencyDirectionTypeAndClientEntity_Id(
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
        return run { modifier(clientStatsEntity) }.let { clientStatsRepository.save(it) }.toDto()   //clientStatsRepository.save(modifier(clientStatsEntity)).toDto()
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

        ClientStatsUpdater(
            exchangeOperation.clientDto!!,
            exchangeOperation.operationType,
            exchangeOperation.giveCurrencyType,
            exchangeOperation.giveAmount,
            exchangeOperation.receiveCurrencyType,
            exchangeOperation.receiveAmount
        )

        when (exchangeOperation.operationType) {
            SELL -> {
                ClientStatsUpdater(
                    exchangeOperation.clientDto!!,
                    exchangeOperation.preExchangeCurrencyType,
                    exchangeOperation.operationType,
                    exchangeOperation.amount
                )
            }

            BUY -> {
                ClientStatsUpdater(
                    exchangeOperation.clientDto!!,
                    exchangeOperation.postExchangeCurrencyType,
                    exchangeOperation.operationType,
                    exchangeOperation.postExchangeAmount
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
            .map { it.total = it.total?.plus(amount); it }
            .orElse(
                ClientStatsEntity(
                    currencyType = currencyType,
                    operationType = operationType,
                    total = amount
                ).apply { this.clientEntity = oldClientDto.toEntity() })
    }
}