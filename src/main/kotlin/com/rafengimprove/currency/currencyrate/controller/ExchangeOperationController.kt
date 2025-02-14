package com.rafengimprove.currency.currencyrate.controller

import com.rafengimprove.currency.currencyrate.model.dto.ExchangeDataDto
import com.rafengimprove.currency.currencyrate.model.dto.ExchangeOperationDto
import com.rafengimprove.currency.currencyrate.service.impl.ExchangeOperationServiceImpl
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/exchange")
class ExchangeOperationController(private val exchangeOperationServiceImpl: ExchangeOperationServiceImpl) {
    private val log = LoggerFactory.getLogger(ExchangeOperationController::class.java)

    @PostMapping
    fun create(@RequestBody exchangeData: ExchangeDataDto): ExchangeOperationDto? {
        log.info("Start to exchange operation by a client with id: ${exchangeData.clientId}")
        return exchangeOperationServiceImpl.exchange(exchangeData)
    }

    @GetMapping()
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

    @DeleteMapping("/{id}")
    fun deleteById(
        @PathVariable("id") id: Long
    ) {
        return exchangeOperationServiceImpl.deleteById(id)
    }
}