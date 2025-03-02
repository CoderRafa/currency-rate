package com.rafengimprove.currency.currencyrate.controller

import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.databind.util.Converter
import com.rafengimprove.currency.currencyrate.model.dto.ExchangeDataDto
import com.rafengimprove.currency.currencyrate.model.dto.ExchangeOperationDto
import com.rafengimprove.currency.currencyrate.model.dto.TopTenExchangeOperations
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType
import com.rafengimprove.currency.currencyrate.model.type.ExchangeOperationSortType
import com.rafengimprove.currency.currencyrate.model.type.OperationType
import com.rafengimprove.currency.currencyrate.service.impl.ExchangeOperationServiceImpl
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/exchange")
class ExchangeOperationController(private val exchangeOperationServiceImpl: ExchangeOperationServiceImpl) {
    private val log = LoggerFactory.getLogger(ExchangeOperationController::class.java)

    @PostMapping
    fun create(@RequestBody exchangeData: ExchangeDataDto): ExchangeOperationDto? {
        log.info("Start to exchange operation by a client with id: ${exchangeData.clientId}")
        return exchangeOperationServiceImpl.exchange(exchangeData)
    }

    @GetMapping
    fun getAll(): List<ExchangeOperationDto> {
        return exchangeOperationServiceImpl.getAll()
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable("id") id: Long): ExchangeOperationDto {
        return exchangeOperationServiceImpl.getById(id)
    }

    @GetMapping("/office/{id}")
    fun getByOfficeId(
        @PathVariable("id") id: Long,
        @PageableDefault(size = 10, page = 0) pageable: Pageable
    ): Page<ExchangeOperationDto> {
        return exchangeOperationServiceImpl.getByOffice(id, pageable)
    }

    @GetMapping("/client/{id}")
    fun getByClientId(
        @PathVariable("id") id: Long,
        @PageableDefault(size = 10, page = 0) pageable: Pageable
    ): Page<ExchangeOperationDto> {
        return exchangeOperationServiceImpl.getByClient(id, pageable)
    }

    @GetMapping("/top-ten/office/{id}")
    fun getTopTenByOfficeId(
        @PathVariable("id") officeId: Long,
        @RequestParam fromCurrencyType: CurrencyType,
        @RequestParam toCurrencyType: CurrencyType,
        @RequestParam operationType: OperationType,
    ): List<ExchangeOperationDto>{
        return exchangeOperationServiceImpl.getTopTenByOffice(fromCurrencyType, toCurrencyType, operationType, officeId)
    }

    @GetMapping("/top-ten/office/{id}/sorted")
    fun getTopTenByOfficeIdSortedBy(
        @PathVariable("id") officeId: Long,
        @RequestParam fromCurrencyType: CurrencyType,
        @RequestParam toCurrencyType: CurrencyType,
        @RequestParam operationType: OperationType,
        @RequestParam sortedBy: List<ExchangeOperationSortType> = listOf()
    ): List<ExchangeOperationDto>{
        return exchangeOperationServiceImpl.getTopTenByOfficeSortedBy(fromCurrencyType, toCurrencyType, operationType, officeId, sortedBy)
    }

    @GetMapping("/top-ten/client/{id}/sorted")
    fun getTopTenByClientIdSortedBy(
        @PathVariable("id") clientId: Long,
        @RequestParam fromCurrencyType: CurrencyType,
        @RequestParam toCurrencyType: CurrencyType,
        @RequestParam operationType: OperationType,
        @RequestParam sortedBy: List<ExchangeOperationSortType> = listOf()
    ): List<ExchangeOperationDto>{
        return exchangeOperationServiceImpl.getTopTenByClientSortedBy(fromCurrencyType, toCurrencyType, operationType, clientId, sortedBy)
    }

    @DeleteMapping("/{id}")
    fun deleteById(
        @PathVariable("id") id: Long
    ) {
        return exchangeOperationServiceImpl.deleteById(id)
    }
}
