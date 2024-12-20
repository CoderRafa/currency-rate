package com.rafengimprove.currency.currencyrate.service

import com.rafengimprove.currency.currencyrate.model.dto.CurrencyRateDto
import com.rafengimprove.currency.currencyrate.model.enumerated.CurrencyType

interface CurrencyRateService {
    fun saveAll(officeId: Long, currencyRates: List<CurrencyRateDto>): List<CurrencyRateDto>
    fun editByType(officeId: Long, currencyRateDto: CurrencyRateDto): CurrencyRateDto?
    fun findAll(officeId: Long): List<CurrencyRateDto>
    fun findByType(officeId: Long, type: CurrencyType): CurrencyRateDto?
}