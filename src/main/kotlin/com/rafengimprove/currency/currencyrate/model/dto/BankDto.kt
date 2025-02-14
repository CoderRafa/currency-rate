package com.rafengimprove.currency.currencyrate.model.dto

import com.rafengimprove.currency.currencyrate.exception.InvalidData
import com.rafengimprove.currency.currencyrate.model.entity.BankEntity
import jakarta.validation.constraints.Min
import kotlin.properties.Delegates

data class BankDto(
    val id: Long? = null,
//    @Size(min = 3, max = Int.MAX_VALUE, message = "Name should longer than 3 letters")
    @field:Min(value = 3, message = "Should be at least 3 letters")
    var name: String,
    var description: String? = null
) {
    init {
        require(name.length > 3)
    }
    var offices: MutableSet<OfficeDto> = mutableSetOf()
}

fun BankDto.toEntity(): BankEntity = BankEntity().apply {
    this.id = this@toEntity.id
    if (this@toEntity.name.isNotBlank()) {
        this.name = this@toEntity.name
    } else {
        throw InvalidData("The bank name can't be blank")
    }
    this.description = this@toEntity.description
    this.officeEntities = this@toEntity.offices.map { it.toEntity() }.toMutableSet()
}


