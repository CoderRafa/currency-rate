package com.rafengimprove.currency.currencyrate.controller

import com.rafengimprove.currency.currencyrate.model.dto.CurrencyRateDto
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType
import com.rafengimprove.currency.currencyrate.service.CurrencyRateService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

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
        log.debug("Update currency rate with from currency type {} in office with id {}", currencyRate.fromCurrencyType, id)
        return currencyRateService.editByType(id, currencyRate)
    }

    @GetMapping("/{id}/currency-rates")
    fun getAll(@PathVariable("id") id: Long): List<CurrencyRateDto> {
        log.debug("Get all currency rates")
        return currencyRateService.findAll(id)
    }

    @DeleteMapping("/delete-currency-rate/{id}")
    fun deleteCurrencyRateById(
        @PathVariable("id") currencyRateId: Long
    ) {
        return currencyRateService.deleteCurrencyRateById(currencyRateId)
    }

//    @GetMapping("/{id}/currency-rate/type")
//    fun getByTypeByOffice(@PathVariable("id") id: Long, @RequestBody type: CurrencyType): CurrencyRateDto? {
//        log.debug("Find the rate of the currency {}", type)
//        return currencyRateService.findBy(id, type)
//    }
}