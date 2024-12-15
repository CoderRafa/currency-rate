package com.rafengimprove.currency.currencyrate.service

import com.rafengimprove.currency.currencyrate.model.dto.OfficeDto
import org.springframework.stereotype.Service

@Service
interface OfficeService {
    fun save(office: OfficeDto): OfficeDto
    fun editByAddress(address: String, office: OfficeDto): OfficeDto?
    fun getByAddress(address: String): OfficeDto?
    fun getAll(): List<OfficeDto>
}