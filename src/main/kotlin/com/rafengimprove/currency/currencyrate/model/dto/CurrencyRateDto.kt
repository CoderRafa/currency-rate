package com.rafengimprove.currency.currencyrate.model.dto

import com.rafengimprove.currency.currencyrate.model.entity.CurrencyRateEntity
import com.rafengimprove.currency.currencyrate.model.entity.OfficeEntity
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType

data class CurrencyRateDto(
    val id: Long? = null,
    val fromCurrencyType: CurrencyType,
    val toCurrencyType: CurrencyType,
    var buyRate: Double,
    var sellRate: Double
) {
    var officeDto: OfficeDto? = null
}

fun CurrencyRateDto.toEntity(officeEntity: OfficeEntity? = null): CurrencyRateEntity = CurrencyRateEntity().apply {
    this.id = this@toEntity.id
    this.fromCurrencyType = this@toEntity.fromCurrencyType
    this.toCurrencyType = this@toEntity.toCurrencyType
    this.buyRate = this@toEntity.buyRate
    this.sellRate = this@toEntity.sellRate
    this.officeEntity = officeEntity ?: this@toEntity.officeDto?.toEntity() ?: throw RuntimeException("The office is still null")
}
