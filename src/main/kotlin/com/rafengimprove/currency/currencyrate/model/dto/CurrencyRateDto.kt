package com.rafengimprove.currency.currencyrate.model.dto

import com.rafengimprove.currency.currencyrate.model.entity.CurrencyRateEntity
import com.rafengimprove.currency.currencyrate.model.enumerated.CurrencyType

data class CurrencyRateDto(
    val id: Long? = null,
    val type: CurrencyType,
    var rate: Double
)

fun CurrencyRateDto.toEntity(): CurrencyRateEntity = CurrencyRateEntity().apply {
    this.id = this@toEntity.id
    this.type = this@toEntity.type
    this.rate = this@toEntity.rate
}
