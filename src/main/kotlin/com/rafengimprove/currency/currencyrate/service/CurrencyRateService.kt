package com.rafengimprove.currency.currencyrate.service

import com.rafengimprove.currency.currencyrate.model.dto.CurrencyRateDto
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable


interface CurrencyRateService {
    fun saveAll(officeId: Long, currencyRates: List<CurrencyRateDto>): List<CurrencyRateDto>
    fun editByType(officeId: Long, currencyRateDto: CurrencyRateDto): CurrencyRateDto?
    fun findAll(officeId: Long): List<CurrencyRateDto>
    fun findBy(officeId: Long, fromCurrencyType: CurrencyType, toCurrencyType: CurrencyType): CurrencyRateDto?
    fun deleteCurrencyRateById(currencyRateId: Long)
//    fun findBy(type: CurrencyType, pageable: Pageable): Page<CurrencyRateDto>
}