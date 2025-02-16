package com.rafengimprove.currency.currencyrate.service

import com.rafengimprove.currency.currencyrate.model.dto.CurrencyRateDto
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType


interface CurrencyRateService {
    fun saveAll(officeId: Long, currencyRates: List<CurrencyRateDto>): List<CurrencyRateDto>
    fun editByType(officeId: Long, currencyRateDto: CurrencyRateDto): CurrencyRateDto?
    fun findAll(officeId: Long): List<CurrencyRateDto>
    fun findBy(officeId: Long, fromCurrencyType: CurrencyType, toCurrencyType: CurrencyType): CurrencyRateDto?
    fun deleteCurrencyRateById(id: Long)
//    fun findBy(type: CurrencyType, pageable: Pageable): Page<CurrencyRateDto>
}
