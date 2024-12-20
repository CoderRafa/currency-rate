package com.rafengimprove.currency.currencyrate.controller

import com.rafengimprove.currency.currencyrate.model.dto.CurrencyRateDto
import com.rafengimprove.currency.currencyrate.model.enumerated.CurrencyType
import com.rafengimprove.currency.currencyrate.service.CurrencyRateService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/office")
class CurrencyRateController(val currencyRateService: CurrencyRateService) {

    private val log = LoggerFactory.getLogger(CurrencyRateController::class.java)

    @PostMapping("/{id}/currency-rates")
    fun save(@PathVariable("id") id: Long, @RequestBody currencyRates: List<CurrencyRateDto>): List<CurrencyRateDto> {
        log.debug("Save a currency rate")
        return currencyRateService.saveAll(id, currencyRates)
    }

    @PutMapping("/{id}/currency-rate")
    fun editByType(
        @PathVariable("id") id: Long,
        @RequestBody currencyRate: CurrencyRateDto
    ): CurrencyRateDto? {
        log.debug("Update currency rate with type {} in office with id {}", currencyRate.type, id)
        return currencyRateService.editByType(id, currencyRate)
    }

    @GetMapping("/{id}/currency-rates")
    fun getAll(@PathVariable("id") id: Long): List<CurrencyRateDto> {
        log.debug("Get all currency rates")
        return currencyRateService.findAll(id)
    }

    @GetMapping("/{id}/currency-rate/type")
    fun getByType(@PathVariable("id") id: Long, @RequestBody type: CurrencyType): CurrencyRateDto? {
        log.debug("Find the rate of the currency {}", type)
        return currencyRateService.findByType(id, type)
    }


}