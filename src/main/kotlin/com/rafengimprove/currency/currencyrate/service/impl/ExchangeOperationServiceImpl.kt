package com.rafengimprove.currency.currencyrate.service.impl

import com.rafengimprove.currency.currencyrate.model.dto.ClientStatsDto
import com.rafengimprove.currency.currencyrate.model.dto.ExchangeOperationDto
import com.rafengimprove.currency.currencyrate.model.dto.toEntity
import com.rafengimprove.currency.currencyrate.model.entity.toDto
import com.rafengimprove.currency.currencyrate.model.type.CurrencyDirectionType
import com.rafengimprove.currency.currencyrate.model.type.CurrencyDirectionType.*
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType
import com.rafengimprove.currency.currencyrate.repository.ExchangeOperationRepository
import com.rafengimprove.currency.currencyrate.service.ExchangeOperationService
import org.springframework.stereotype.Service

@Service
class ExchangeOperationServiceImpl(
    val clientStatsServiceImpl: ClientStatsServiceImpl,
    val exchangeOperationRepository: ExchangeOperationRepository
) : ExchangeOperationService {

    override fun save(exchangeOperationDto: ExchangeOperationDto): ExchangeOperationDto {
        val exchangeOperation = exchangeOperationRepository.save(exchangeOperationDto.toEntity()).toDto()

        getClientIdCurrencyTypeDirectionAndAmountFromExchangeOperation(
            clientStatsServiceImpl, exchangeOperation
        )

        return exchangeOperation
    }
}

fun getClientIdCurrencyTypeDirectionAndAmountFromExchangeOperation(
    clientStatsServiceImpl: ClientStatsServiceImpl,
    exchangeOperation: ExchangeOperationDto,
) {
    val id = exchangeOperation.clientDto?.id
    lateinit var currencyType: CurrencyType
    lateinit var currencyDirectionType: CurrencyDirectionType
    var amount: Double = 0.0

    if (exchangeOperation.currencyDirectionType == SELL) {
        currencyDirectionType = SELL
        currencyType = exchangeOperation.preExchangeCurrencyType
        amount = exchangeOperation.preExchangeAmount
    } else {
        currencyDirectionType = BUY
        currencyType = exchangeOperation.postExchangeCurrencyType
        amount = exchangeOperation.postExchangeAmount
    }

    updateAmountInClientStats(
        clientStatsServiceImpl, exchangeOperation,
        id!!, currencyType, currencyDirectionType,
        amount
    )
}

fun updateAmountInClientStats(
    clientStatsServiceImpl: ClientStatsServiceImpl,
    exchangeOperation: ExchangeOperationDto,
    id: Long, currencyType: CurrencyType,
    currencyDirectionType: CurrencyDirectionType, amount: Double
) {
    val clientStats =
        clientStatsServiceImpl
            .getClientStatsByClientIdCurrencyTypeAndDirection(
                id, currencyType, currencyDirectionType
            )

    if (clientStats != null) {
        clientStatsServiceImpl.editByClientIdCurrencyTypeAndDirection(
            id, currencyType, currencyDirectionType, amount
        )
    } else {
        val clientStats = ClientStatsDto(
            currencyType = currencyType,
            currencyDirectionType = currencyDirectionType,
            total = amount
        )

        clientStats.clientDto = exchangeOperation.clientDto

        clientStatsServiceImpl.save(clientStats)
    }
}