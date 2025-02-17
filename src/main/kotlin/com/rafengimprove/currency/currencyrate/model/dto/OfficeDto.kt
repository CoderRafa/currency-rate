package com.rafengimprove.currency.currencyrate.model.dto

import com.rafengimprove.currency.currencyrate.model.entity.BankEntity
import com.rafengimprove.currency.currencyrate.model.entity.CurrencyRateEntity
import com.rafengimprove.currency.currencyrate.model.entity.ExchangeOperationEntity
import com.rafengimprove.currency.currencyrate.model.entity.OfficeEntity

data class OfficeDto(
    val id: Long? = null,
    var address: String,
    var description: String? = null,
    var area: Double
) {
    var bank: BankDto? = null
    var currencyRates: MutableSet<CurrencyRateDto> = mutableSetOf()
    var exchangeOperation: ExchangeOperationDto? = null
}

fun OfficeDto.toEntity(
    bank: BankEntity? = null,
    currencies: MutableSet<CurrencyRateEntity>? = null,
    exchangeOperation: ExchangeOperationEntity? = null): OfficeEntity = OfficeEntity().apply {
    this.id = this@toEntity.id
    this.address = this@toEntity.address
    this.description = this@toEntity.description
    this.area = this@toEntity.area
    this.bankEntity = bank ?: this@toEntity.bank?.toEntity() ?: throw RuntimeException("Bank is still null")
    this.currencyRateEntities = currencies ?: this@toEntity.currencyRates.map { it.toEntity() }.toMutableSet()
}