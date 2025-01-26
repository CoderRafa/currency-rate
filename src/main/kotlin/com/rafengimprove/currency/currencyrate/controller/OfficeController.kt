package com.rafengimprove.currency.currencyrate.controller

import com.rafengimprove.currency.currencyrate.model.dto.OfficeDto
import com.rafengimprove.currency.currencyrate.model.type.OperationType
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType
import com.rafengimprove.currency.currencyrate.service.OfficeService
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class OfficeController(val officeService: OfficeService) {
    private val log = LoggerFactory.getLogger(OfficeController::class.java)

    @PostMapping("/bank/{id}/office")
    fun save(@PathVariable("id") id: Long, @RequestBody offices: List<OfficeDto>): List<OfficeDto> {
        return officeService.save(id, offices)
    }

    @GetMapping("/bank/{id}/office")
    fun getAllOfficesOfBank(@PathVariable("id") id: Long): List<OfficeDto> {
        return officeService.getAllByBank(id)
    }

    @GetMapping("/office/{id}")
    fun getOfficeById(@PathVariable("id") id: Long): OfficeDto? {
        return officeService.getById(id)
    }

    @PutMapping("/bank/{id}/office")
    fun editOfficeById(
        @PathVariable("id") id: Long,
        @RequestBody office: OfficeDto): OfficeDto? {
        return officeService.editById(id, office)
    }

    @GetMapping("/office")
    fun getByType(
        @RequestParam currencyType: CurrencyType,
        @RequestParam("operationType") operationType: OperationType,
        @PageableDefault(size = 10, page = 0) pageable: Pageable
    ): Page<OfficeDto> {
        log.info("Find rates of the currency type {} with direction: {}", currencyType, operationType)

        return officeService.findOfficesBy(currencyType, operationType, pageable)
    }

    @GetMapping("/office/work_with")
    fun getOfficeWorkingWithType(
        @RequestParam currencyType: CurrencyType,
        @PageableDefault(size = 10, page = 0) pageable: Pageable
    ): Page<OfficeDto> {
        log.info("Find offices that work with currency {}", currencyType)

        return officeService.findOfficesWorkingWithType(currencyType, pageable)
    }
}