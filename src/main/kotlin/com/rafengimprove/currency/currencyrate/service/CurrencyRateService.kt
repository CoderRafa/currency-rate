package com.rafengimprove.currency.currencyrate.service

import com.rafengimprove.currency.currencyrate.model.dto.CurrencyRateDto
import com.rafengimprove.currency.currencyrate.model.enumerated.CurrencyType

interface CurrencyRateService {
    fun save(currencyList: CurrencyRateDto): CurrencyRateDto
    fun editByType(type: CurrencyType, rate: Double): CurrencyRateDto?
    fun findAll(): List<CurrencyRateDto>
    fun findByType(type: CurrencyType): CurrencyRateDto?
    fun deleteByType(type: CurrencyType): List<CurrencyRateDto>
}