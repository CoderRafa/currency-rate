package com.rafengimprove.currency.currencyrate.model.dto

import com.rafengimprove.currency.currencyrate.model.entity.CurrencyRateEntity
import com.rafengimprove.currency.currencyrate.model.entity.OfficeEntity
import com.rafengimprove.currency.currencyrate.model.enumerated.CurrencyType

data class CurrencyRateDto(
    val id: Long? = null,
    val type: CurrencyType,
    var rate: Double
) {
    var officeDto: OfficeDto? = null
}

fun CurrencyRateDto.toEntity(officeEntity: OfficeEntity? = null): CurrencyRateEntity = CurrencyRateEntity().apply {
    this.id = this@toEntity.id
    this.type = this@toEntity.type
    this.rate = this@toEntity.rate
    this.officeEntity = officeEntity ?: this@toEntity.officeDto?.toEntity() ?: throw RuntimeException("The office is still null")
}
