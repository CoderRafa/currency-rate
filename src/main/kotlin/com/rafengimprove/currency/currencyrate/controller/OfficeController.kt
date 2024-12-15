package com.rafengimprove.currency.currencyrate.controller

import com.rafengimprove.currency.currencyrate.model.dto.OfficeDto
import com.rafengimprove.currency.currencyrate.service.OfficeService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/office")
class OfficeController(val officeService: OfficeService) {

    @PostMapping
    fun save(@RequestBody office: OfficeDto): OfficeDto {
        return officeService.save(office)
    }

    @GetMapping
    fun getAllOffices(): List<OfficeDto> {
        return officeService.getAll()
    }

    @GetMapping("/{address}")
    fun getOfficeByAddress(@PathVariable("address") address: String): OfficeDto? {
        return officeService.getByAddress(address)
    }

    @PutMapping("/{address}")
    fun editOfficeByAddress(
        @PathVariable("address") address: String,
        @RequestBody office: OfficeDto): OfficeDto? {
        return officeService.editByAddress(address, office)
    }
}