package com.rafengimprove.currency.currencyrate.service

import com.rafengimprove.currency.currencyrate.model.dto.ClientStatsDto
import com.rafengimprove.currency.currencyrate.model.type.CurrencyDirectionType
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType

interface ClientStatsService {
    fun save(clientStatsDto: ClientStatsDto): ClientStatsDto

    fun getClientStatsByClientIdCurrencyTypeAndDirection(
        id: Long,
        currencyType: CurrencyType,
        currencyDirectionType: CurrencyDirectionType
    ): ClientStatsDto?

    fun editByClientIdCurrencyTypeAndDirection(
        id: Long,
        currencyType: CurrencyType,
        currencyDirectionType: CurrencyDirectionType,
        amount: Double
    ): ClientStatsDto?
}