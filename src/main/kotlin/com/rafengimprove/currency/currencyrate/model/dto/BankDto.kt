package com.rafengimprove.currency.currencyrate.model.dto

import com.rafengimprove.currency.currencyrate.model.entity.BankEntity

data class BankDto(
    val id: Long? = null,
    var name: String,
    var description: String? = null
) {
    var offices: MutableSet<OfficeDto> = mutableSetOf()
}

fun BankDto.toEntity(): BankEntity = BankEntity().apply {
    this.id = this@toEntity.id
    this.name = this@toEntity.name
    this.description = this@toEntity.description
    this.officeEntities = this@toEntity.offices.map { it.toEntity() }.toMutableSet()
}


