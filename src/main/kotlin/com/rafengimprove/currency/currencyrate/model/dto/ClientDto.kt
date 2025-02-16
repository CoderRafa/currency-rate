package com.rafengimprove.currency.currencyrate.model.dto

import com.rafengimprove.currency.currencyrate.model.entity.ClientEntity
import com.rafengimprove.currency.currencyrate.model.entity.ExchangeOperationEntity

/**
 * DTO for {@link com.rafengimprove.currency.currencyrate.model.entity.ClientEntity}
 */
data class ClientDto(
    val id: Long? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val passportNumber: String? = null,
    val email: String? = null
) {
    val exchangeOperations: MutableSet<ExchangeOperationDto> = mutableSetOf()
}

fun ClientDto.toEntity(exchangeOperations: MutableSet<ExchangeOperationEntity>? = null) = ClientEntity().apply {
    this.id = this@toEntity.id
    this.firstName = this@toEntity.firstName
    this.lastName = this@toEntity.lastName
    this.passportNumber = this@toEntity.passportNumber
    this.email = this@toEntity.email
}