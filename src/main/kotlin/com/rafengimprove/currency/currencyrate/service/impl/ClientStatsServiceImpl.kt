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
        fromCurrencyType: CurrencyType,
        toCurrencyType: CurrencyType,
        operationType: OperationType,
    ): Optional<ClientStatsEntity> {
        return clientStatsRepository.findByFromCurrencyTypeAndToCurrencyTypeAndOperationTypeAndClientEntity_Id(
            fromCurrencyType,
            toCurrencyType,
            operationType,
            clientId,
        )
    }

    override fun getClientStatsBy(
        clientId: Long,
        fromCurrencyType: CurrencyType,
        toCurrencyType: CurrencyType,
        operationType: OperationType,
        converter: (ClientStatsEntity) -> ClientStatsDto?
    ): ClientStatsDto? {
        return clientStatsRepository.findByFromCurrencyTypeAndToCurrencyTypeAndOperationTypeAndClientEntity_Id(
            fromCurrencyType,
            toCurrencyType,
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
            .toDto()   //clientStatsRepository.save(modifier(clientStatsEntity)).toDto() // TODO: Убираем
    }

    override fun modifyClientStats(
        exchangeOperation: ExchangeDataDto,
    ) {

        when (exchangeOperation.operationType) {
            SELL -> {
                ClientStatsUpdater(
                    clientService.findById(exchangeOperation.clientId),
                    exchangeOperation.fromCurrencyType,
                    exchangeOperation.toCurrencyType,
                    exchangeOperation.operationType,
                    exchangeOperation.giveAmount
                )
            }

            BUY -> {
                ClientStatsUpdater(
                    clientService.findById(exchangeOperation.clientId),
                    exchangeOperation.fromCurrencyType,
                    exchangeOperation.toCurrencyType,
                    exchangeOperation.operationType,
                    exchangeOperation.receiveAmount
                )
            }
        }.update {
            getClientStatsBy(
                it.oldClientDto.id!!,
                it.fromCurrencyType,
                it.toCurrencyType,
                it.operationType
            )
        }.let { clientStatsRepository.save(it) }
    }
}

class ClientStatsUpdater(
    val oldClientDto: ClientDto,
    val fromCurrencyType: CurrencyType,
    val toCurrencyType: CurrencyType,
    val operationType: OperationType,
    private val amount: Double
) {
    fun update(clientStatsLoader: (ClientStatsUpdater) -> Optional<ClientStatsEntity>): ClientStatsEntity {
        return clientStatsLoader(this)
            .map { it.total = it.total.plus(amount); it }
            .orElse(
                ClientStatsEntity(
                    fromCurrencyType = fromCurrencyType,
                    toCurrencyType = toCurrencyType,
                    operationType = operationType,
                    total = amount
                ).apply { this.clientEntity = oldClientDto.toEntity() })
    }
}