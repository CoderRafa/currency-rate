package com.rafengimprove.currency.currencyrate.service

import com.rafengimprove.currency.currencyrate.model.dto.OfficeDto
import com.rafengimprove.currency.currencyrate.model.enumerated.CurrencyType
import org.springframework.stereotype.Service

@Service
interface OfficeService {
    fun save(bankId: Long, offices: List<OfficeDto>): List<OfficeDto>
    fun editById(bankId: Long, office: OfficeDto): OfficeDto?
    fun getById(officeId: Long): OfficeDto?
    fun getAllByBank(bankId: Long): List<OfficeDto>
}