package com.rafengimprove.currency.currencyrate.model.dto

import com.rafengimprove.currency.currencyrate.model.entity.ClientEntity
import com.rafengimprove.currency.currencyrate.model.entity.ExchangeOperationEntity
import com.rafengimprove.currency.currencyrate.model.entity.OfficeEntity
import com.rafengimprove.currency.currencyrate.model.type.CurrencyDirectionType
import com.rafengimprove.currency.currencyrate.model.type.CurrencyDirectionType.*
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType.*
import java.io.Serializable
import java.time.LocalDateTime

/**
 * DTO for {@link com.rafengimprove.currency.currencyrate.model.entity.ExchangeOperationEntity}
 */
data class ExchangeOperationDto(
    val id: Long? = null,
    val currencyDirectionType: CurrencyDirectionType = SELL,
    val preExchangeAmount: Double = 0.0,
    val preExchangeCurrencyType: CurrencyType = USD,
    val postExchangeAmount: Double = 0.0,
    val postExchangeCurrencyType: CurrencyType = RUB,
    val dateAndTimeOfExchange: LocalDateTime = LocalDateTime.now()
) {
    lateinit var officeDto: OfficeDto
    var clientDto: ClientDto? = null
}

fun ExchangeOperationDto.toEntity(officeEntity: OfficeEntity? = null, clientEntity: ClientEntity? = null) = ExchangeOperationEntity().apply {
    this.id = this@toEntity.id
    this.currencyDirectionType = this@toEntity.currencyDirectionType
    this.preExchangeAmount = this@toEntity.preExchangeAmount
    this.postExchangeAmount = this@toEntity.postExchangeAmount
    this.postExchangeCurrencyType = this@toEntity.postExchangeCurrencyType
    this.dateAndTimeOfExchange = this@toEntity.dateAndTimeOfExchange
    this.officeEntity = officeEntity ?: this@toEntity.officeDto.toEntity()
    this.clientEntity = clientEntity ?: this@toEntity.clientDto?.toEntity() ?: throw RuntimeException("The client is still null")
}