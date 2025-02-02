package com.rafengimprove.currency.currencyrate.controller

import com.rafengimprove.currency.currencyrate.model.dto.BankDto
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType
import com.rafengimprove.currency.currencyrate.service.BankService
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/bank")
class BankController(val bankService: BankService) {

    private val log = LoggerFactory.getLogger(BankController::class.java)

    @PostMapping
    fun save(@RequestBody bankDto: BankDto): BankDto {
        log.debug("Save a new bank")
        return bankService.save(bankDto)
    }

    @GetMapping
    fun getAllBanks(): List<BankDto> {
        log.debug("Get all banks")
        return bankService.getAll()
    }

    @GetMapping("/{name}")
    fun getBankByName(@PathVariable("name") name: String): BankDto? {
        return bankService.getByName(name)
    }

    @GetMapping("/currency")
    fun getBanksByCurrencyType(
        @RequestParam currencyType: CurrencyType,
        @PageableDefault(size = 10, page = 0) pageable: Pageable
    ): Page<BankDto> {
        return bankService.getAllByCurrency(currencyType, pageable)
    }

//    @GetMapping("/no-currency")
//    fun getBanksThatDoNotWorkWithCurrency(
//        @RequestParam currencyType: CurrencyType,
//        @PageableDefault(size = 10, page = 0) pageable: Pageable
//    ): Page<BankDto> {
//        return bankService.getAllBanksThatDoNotWorkWithCurrency(currencyType, pageable)
//    }

    @PutMapping("/{name}")
    fun editBankByName(@PathVariable("name") name: String, @RequestBody bankDto: BankDto): BankDto? {
        return bankService.editByName(name, bankDto)
    }

    @DeleteMapping("/{name}")
    fun deleteBankByName(@PathVariable("name") name: String) {
        return bankService.deleteBankByName(name)
    }
}