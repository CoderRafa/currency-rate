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
@RequestMapping("/api/v1/currency-rate")
class CurrencyRateController(val currencyRateService: CurrencyRateService) {

    private val log = LoggerFactory.getLogger(CurrencyRateController::class.java)

    @PostMapping
    fun save(@RequestBody currencyRate: CurrencyRateDto): CurrencyRateDto {
        log.debug("Save a currency rate")
        return currencyRateService.save(currencyRate)
    }

    @PutMapping("/{type}/{rate}")
    fun editByType(
        @PathVariable("type") type: CurrencyType,
        @PathVariable("rate") rate: Double
    ): CurrencyRateDto? {
        log.debug("Update currency rate with type {}", type)
        return currencyRateService.editByType(type, rate)
    }

    @GetMapping
    fun getAll(): List<CurrencyRateDto> {
        log.debug("Get all currency rates")
        return currencyRateService.findAll()
    }

    @GetMapping("/{type}")
    fun getByType(@PathVariable("type") type: CurrencyType): CurrencyRateDto? {
        log.debug("Find the rate of the currency {}", type)
        return currencyRateService.findByType(type)
    }

    @DeleteMapping("/{type}")
    fun deleteByType(@PathVariable("type") type: CurrencyType): List<CurrencyRateDto> {
        log.debug("Delete currency rate with type {}", type)
        return currencyRateService.deleteByType(type)
    }

}