package com.rafengimprove.currency.currencyrate.controller

import com.rafengimprove.currency.currencyrate.model.dto.ClientStatsDto
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType
import com.rafengimprove.currency.currencyrate.model.type.OperationType
import com.rafengimprove.currency.currencyrate.service.ClientStatsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/client-stats")
class ClientStatsController(val clientStatsService: ClientStatsService) {

    @GetMapping("/max")
    fun findClientWithMaxTotal(
        @RequestParam fromCurrencyType: CurrencyType,
        @RequestParam toCurrencyType: CurrencyType,
        @RequestParam operationType: OperationType,
    ): List<ClientStatsDto> {
        return clientStatsService.findClientWhoHasMaxTotal(fromCurrencyType, toCurrencyType, operationType)
    }
}