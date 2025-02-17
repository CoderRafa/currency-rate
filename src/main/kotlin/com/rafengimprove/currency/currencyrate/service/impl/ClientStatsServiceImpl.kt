package com.rafengimprove.currency.currencyrate.service.impl

import com.rafengimprove.currency.currencyrate.model.dto.*
import com.rafengimprove.currency.currencyrate.model.entity.ClientEntity
import com.rafengimprove.currency.currencyrate.model.entity.ClientStatsEntity
import com.rafengimprove.currency.currencyrate.model.entity.toDto
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType
import com.rafengimprove.currency.currencyrate.model.type.OperationType
import com.rafengimprove.currency.currencyrate.model.type.OperationType.BUY
import com.rafengimprove.currency.currencyrate.model.type.OperationType.SELL
import com.rafengimprove.currency.currencyrate.repository.ClientRepository
import com.rafengimprove.currency.currencyrate.repository.ClientStatsRepository
import com.rafengimprove.currency.currencyrate.service.ClientStatsService
import org.springframework.stereotype.Service
import java.util.*

@Service
class ClientStatsServiceImpl(
    val clientStatsRepository: ClientStatsRepository,
    val clientRepository: ClientRepository
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
            .toDto()
    }

    override fun modifyClientStats(
        exchangeOperation: ExchangeDataDto,
    ) {

        when (exchangeOperation.operationType) {
            SELL -> {
                ClientStatsUpdater(
                    clientRepository.findById(exchangeOperation.clientId),
                    exchangeOperation.fromCurrencyType,
                    exchangeOperation.toCurrencyType,
                    exchangeOperation.operationType,
                    exchangeOperation.giveAmount
                )
            }

            BUY -> {
                ClientStatsUpdater(
                    clientRepository.findById(exchangeOperation.clientId),
                    exchangeOperation.fromCurrencyType,
                    exchangeOperation.toCurrencyType,
                    exchangeOperation.operationType,
                    exchangeOperation.receiveAmount
                )
            }
        }.update {
            getClientStatsBy(
                it.oldClientEntity.get().id!!,
                it.fromCurrencyType,
                it.toCurrencyType,
                it.operationType
            )
        }.let { clientStatsRepository.save(it) }
    }

    override fun findClientWhoHasMaxTotal(
        fromCurrencyType: CurrencyType,
        toCurrencyType: CurrencyType,
        operationType: OperationType
    ): List<ClientStatsDto> {
        return clientStatsRepository.findClientWhoHasMaxTotal(fromCurrencyType, toCurrencyType, operationType).map { it.toDto() }
    }
}

class ClientStatsUpdater(
    val oldClientEntity: Optional<ClientEntity>,
    val fromCurrencyType: CurrencyType,
    val toCurrencyType: CurrencyType,
    val operationType: OperationType,
    private val amount: Double
) {
    fun update(clientStatsLoader: (ClientStatsUpdater) -> Optional<ClientStatsEntity>): ClientStatsEntity {
        return oldClientEntity.map {
            clientStatsLoader(this)
                .map { it.total = it.total.plus(amount); it }
                .orElse(
                    ClientStatsEntity(
                        fromCurrencyType = fromCurrencyType,
                        toCurrencyType = toCurrencyType,
                        operationType = operationType,
                        total = amount
                    ).apply { this.clientEntity = it })
        }.orElseThrow()
    }
}