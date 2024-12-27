package com.rafengimprove.currency.currencyrate.service

import com.rafengimprove.currency.currencyrate.model.dto.CurrencyRateDto
import com.rafengimprove.currency.currencyrate.model.enumerated.CurrencyType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable


interface CurrencyRateService {
    fun saveAll(officeId: Long, currencyRates: List<CurrencyRateDto>): List<CurrencyRateDto>
    fun editByType(officeId: Long, currencyRateDto: CurrencyRateDto): CurrencyRateDto?
    fun findAll(officeId: Long): List<CurrencyRateDto>
    fun findByTypeByOffice(officeId: Long, type: CurrencyType): CurrencyRateDto?
    fun findByType(type: CurrencyType, pageable: Pageable): Page<CurrencyRateDto>
}