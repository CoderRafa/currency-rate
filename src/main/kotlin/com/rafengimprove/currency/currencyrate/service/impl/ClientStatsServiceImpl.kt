package com.rafengimprove.currency.currencyrate.service.impl

import com.rafengimprove.currency.currencyrate.model.dto.ClientDto
import com.rafengimprove.currency.currencyrate.model.dto.ClientStatsDto
import com.rafengimprove.currency.currencyrate.model.dto.ExchangeOperationDto
import com.rafengimprove.currency.currencyrate.model.dto.toEntity
import com.rafengimprove.currency.currencyrate.model.entity.ClientStatsEntity
import com.rafengimprove.currency.currencyrate.model.entity.toDto
import com.rafengimprove.currency.currencyrate.model.type.CurrencyDirectionType
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType
import com.rafengimprove.currency.currencyrate.repository.ClientStatsRepository
import com.rafengimprove.currency.currencyrate.service.ClientStatsService
import org.springframework.stereotype.Service

@Service
class ClientStatsServiceImpl(val clientStatsRepository: ClientStatsRepository) : ClientStatsService {
    override fun save(clientStatsDto: ClientStatsDto): ClientStatsDto {
        return clientStatsRepository.save(clientStatsDto.toEntity()).toDto()
    }

    override fun getClientStatsBy(
        clientId: Long,
        currencyType: CurrencyType,
        currencyDirectionType: CurrencyDirectionType,
        converter: (ClientStatsEntity) -> ClientStatsDto?
    ): ClientStatsDto? {
        return converter(
            clientStatsRepository.findByClientIdAndCurrencyTypeAndCurrencyDirectionType(
                clientId,
                currencyType,
                currencyDirectionType
            )
        )
    }

    override fun editClientStatsBy(
        clientStatsEntity: ClientStatsEntity,
        amount: Double,
        modifier: (ClientStatsEntity) -> ClientStatsEntity
    ): ClientStatsDto? {
        return run { modifier(clientStatsEntity) }.let { clientStatsRepository.save(it) }.toDto()   //clientStatsRepository.save(modifier(clientStatsEntity)).toDto()
    }

    fun getClientIdCurrencyTypeDirectionAndAmountFromExchangeOperation(
        exchangeOperation: ExchangeOperationDto,
    ) {
        val id = exchangeOperation.clientDto?.id
        lateinit var currencyType: CurrencyType
        lateinit var currencyDirectionType: CurrencyDirectionType
        var amount: Double = 0.0

        when (exchangeOperation.currencyDirectionType) {
            CurrencyDirectionType.SELL -> {
                currencyDirectionType = CurrencyDirectionType.SELL
                currencyType = exchangeOperation.preExchangeCurrencyType
                amount = exchangeOperation.preExchangeAmount
            }

            CurrencyDirectionType.BUY -> {
                currencyDirectionType = CurrencyDirectionType.BUY
                currencyType = exchangeOperation.postExchangeCurrencyType
                amount = exchangeOperation.postExchangeAmount
            }
        }
        updateAmountInClientStats(
            exchangeOperation,
            id!!, currencyType, currencyDirectionType,
            amount
        )
    }

    fun updateAmountInClientStats(
        exchangeOperation: ExchangeOperationDto,
        clientId: Long, currencyType: CurrencyType,
        currencyDirectionType: CurrencyDirectionType, amount: Double
    ) {
        val clientStats =
          getClientStatsBy(
                    clientId, currencyType, currencyDirectionType
                )

        save(clientStats)
    }
}

class ClientStatsUpdater(
    private val currencyType: CurrencyType,
    private val currencyDirectionType: CurrencyDirectionType,
    private val clientDto: ClientDto,
    private val amount: Double
) {
    fun update(clientStatsLoader: () -> ClientStatsDto?): ClientStatsDto {
        val clientStatsDto = clientStatsLoader()
        return if (clientStatsDto != null) {
            clientStatsDto.total = clientStatsDto.total?.plus(amount)
            clientStatsDto
        } else {
            ClientStatsDto(
                currencyType = currencyType,
                currencyDirectionType = currencyDirectionType,
                total = amount
            ).apply { this.clientDto = clientDto }
        }
    }
}