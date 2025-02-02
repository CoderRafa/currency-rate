package com.rafengimprove.currency.currencyrate.model.dto

import com.rafengimprove.currency.currencyrate.model.entity.ClientStatsEntity
import com.rafengimprove.currency.currencyrate.model.type.OperationType
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType

/**
 * DTO for {@link com.rafengimprove.currency.currencyrate.model.entity.ClientStatsEntity}
 */
data class ClientStatsDto(
    val id: Long? = null,
    val currencyType: CurrencyType? = null,
    val operationType: OperationType? = null,
    var total: Double = 0.0
) {
    var clientDto: ClientDto? = null
}

fun ClientStatsDto.toEntity() = ClientStatsEntity().apply {
    this.id = this@toEntity.id
    this.currencyType = this@toEntity.currencyType
    this.operationType = this@toEntity.operationType
    this.total = this@toEntity.total
    this.clientEntity = this@toEntity.clientDto?.toEntity()
}