package com.rafengimprove.currency.currencyrate.controller

import com.rafengimprove.currency.currencyrate.model.dto.OfficeDto
import com.rafengimprove.currency.currencyrate.service.OfficeService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class OfficeController(val officeService: OfficeService) {

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
}