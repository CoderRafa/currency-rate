package com.rafengimprove.currency.currencyrate.service

import com.rafengimprove.currency.currencyrate.model.dto.OfficeDto
import org.springframework.stereotype.Service

@Service
interface OfficeService {
    fun save(bankId: Long, office: OfficeDto): OfficeDto
    fun editById(bankId: Long, office: OfficeDto): OfficeDto?
    fun getById(officeId: Long): OfficeDto?
    fun getAllByBank(bankId: Long): List<OfficeDto>
}