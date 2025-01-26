package com.rafengimprove.currency.currencyrate.service.impl

import com.rafengimprove.currency.currencyrate.model.dto.ClientDto
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

        modifyClientStats(exchangeOperation)

        return exchangeOperation
    }
}

fun modifyClientStats(
    exchangeOperation: ExchangeOperationDto,
) {
    val id = exchangeOperation.clientDto?.id
    lateinit var currencyType: CurrencyType
    lateinit var currencyDirectionType: CurrencyDirectionType
    var amount: Double = 0.0

    when (exchangeOperation.currencyDirectionType) {
        SELL -> {
            currencyDirectionType = SELL
            currencyType = exchangeOperation.preExchangeCurrencyType
            amount = exchangeOperation.preExchangeAmount
        }

        BUY -> {
            currencyDirectionType = BUY
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
        clientStatsServiceImpl
            .getClientStatsBy(
                clientId, currencyType, currencyDirectionType
            )
    
        clientStatsServiceImpl.save(clientStats)
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