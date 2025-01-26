package com.rafengimprove.currency.currencyrate.controller

import com.rafengimprove.currency.currencyrate.model.dto.ExchangeDataDto
import com.rafengimprove.currency.currencyrate.service.impl.ExchangeOperationServiceImpl
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/exchange")
class ExchangeOperationController(private val exchangeOperationServiceImpl: ExchangeOperationServiceImpl) {
    private val log = LoggerFactory.getLogger(ExchangeOperationController::class.java)

    @PostMapping
    fun create(@RequestBody exchangeData: ExchangeDataDto): Boolean {
        log.info("Start to exchange operation by a client with id: ${exchangeData.clientId}")
        return exchangeOperationServiceImpl.exchange(exchangeData)
    }
}