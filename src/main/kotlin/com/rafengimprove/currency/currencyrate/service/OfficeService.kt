package com.rafengimprove.currency.currencyrate.service

import com.rafengimprove.currency.currencyrate.model.dto.OfficeDto
import com.rafengimprove.currency.currencyrate.model.entity.OfficeEntity
import com.rafengimprove.currency.currencyrate.model.type.CurrencyDirectionType
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
interface OfficeService {
    fun save(bankId: Long, offices: List<OfficeDto>): List<OfficeDto>
    fun editById(bankId: Long, office: OfficeDto): OfficeDto?
    fun getById(officeId: Long): OfficeDto?
    fun getAllByBank(bankId: Long): List<OfficeDto>
    fun findOfficesBy(currencyType: CurrencyType, currencyDirectionType: CurrencyDirectionType, pageable: Pageable): Page<OfficeDto>
    fun findOfficesWorkingWithType(type: CurrencyType, pageable: Pageable): Page<OfficeDto>
}