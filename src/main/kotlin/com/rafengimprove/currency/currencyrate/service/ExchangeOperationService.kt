package com.rafengimprove.currency.currencyrate.service

import com.rafengimprove.currency.currencyrate.model.dto.ExchangeDataDto
import com.rafengimprove.currency.currencyrate.model.dto.ExchangeOperationDto
import com.rafengimprove.currency.currencyrate.model.dto.TopTenExchangeOperations
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType
import com.rafengimprove.currency.currencyrate.model.type.ExchangeOperationSortType
import com.rafengimprove.currency.currencyrate.model.type.OperationType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime

interface ExchangeOperationService {
//   fun add(exchangeOperationDto: ExchangeDataDto)
    fun exchange(exchangeDataDto: ExchangeDataDto): ExchangeOperationDto?

    fun getAll(): List<ExchangeOperationDto>

    fun getById(id: Long): ExchangeOperationDto

    fun deleteById(id: Long)

    fun getByOffice(id: Long, pageable: Pageable): Page<ExchangeOperationDto>

    fun getByClient(id: Long, pageable: Pageable): Page<ExchangeOperationDto>

    fun getTopTenByOffice(
        fromCurrencyType: CurrencyType,
        toCurrencyType: CurrencyType,
        operationType: OperationType,
        officeId: Long
    ): List<ExchangeOperationDto>

    fun getTopTenByOfficeSortedBy(
    fromCurrencyType: CurrencyType,
    toCurrencyType: CurrencyType,
    operationType: OperationType,
    officeId: Long,
    sortedBy: List<ExchangeOperationSortType>
    ): List<ExchangeOperationDto>

    fun getTopTenByClientSortedBy(
    fromCurrencyType: CurrencyType,
    toCurrencyType: CurrencyType,
    operationType: OperationType,
    clientId: Long,
    sortedBy: List<ExchangeOperationSortType>
    ): List<ExchangeOperationDto>
}