package com.rafengimprove.currency.currencyrate.service

import com.rafengimprove.currency.currencyrate.model.dto.ClientStatsDto
import com.rafengimprove.currency.currencyrate.model.entity.ClientStatsEntity
import com.rafengimprove.currency.currencyrate.model.entity.toDto
import com.rafengimprove.currency.currencyrate.model.type.CurrencyDirectionType
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType

interface ClientStatsService {
    fun save(clientStatsDto: ClientStatsDto): ClientStatsDto

    fun getClientStatsBy(
        clientId: Long,
        currencyType: CurrencyType,
        currencyDirectionType: CurrencyDirectionType,
        converter: (ClientStatsEntity) -> ClientStatsDto? = { it.toDto() }
    ): ClientStatsDto?

    fun editClientStatsBy(
        clientStatsEntity: ClientStatsEntity,
        amount: Double,
        modifier: (ClientStatsEntity) -> ClientStatsEntity = { it.total = it.total?.plus(amount); it }
    ): ClientStatsDto?
}