package com.rafengimprove.currency.currencyrate.model.dto

import com.rafengimprove.currency.currencyrate.model.entity.ClientStatsEntity
import com.rafengimprove.currency.currencyrate.model.type.CurrencyDirectionType
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType
import java.io.Serializable

/**
 * DTO for {@link com.rafengimprove.currency.currencyrate.model.entity.ClientStatsEntity}
 */
data class ClientStatsDto(
    val id: Long? = null,
    val currencyType: CurrencyType? = null,
    val currencyDirectionType: CurrencyDirectionType? = null,
    var total: Double? = null
) {
    var clientDto: ClientDto? = null
}

fun ClientStatsDto.toEntity() = ClientStatsEntity().apply {
    this.id = this@toEntity.id
    this.currencyType = this@toEntity.currencyType
    this.currencyDirectionType = this@toEntity.currencyDirectionType
    this.total = this@toEntity.total
    this.clientEntity = this@toEntity.clientDto?.toEntity()
}