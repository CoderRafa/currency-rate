package com.rafengimprove.currency.currencyrate.model.dto

import com.rafengimprove.currency.currencyrate.model.dto.OfficeDto
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType
import com.rafengimprove.currency.currencyrate.model.type.OperationType
import java.io.Serializable
import java.time.LocalDateTime

/**
 * DTO for {@link com.rafengimprove.currency.currencyrate.model.entity.ExchangeOperationEntity}
 */
data class ExchangeOperationDto(
    val id: Long? = null,
    val operationType: OperationType? = null,
    val giveAmount: Double = 0.0,
    val fromCurrencyType: CurrencyType? = null,
    val receiveAmount: Double = 0.0,
    val toCurrencyType: CurrencyType? = null,
    val dateAndTimeOfExchange: LocalDateTime? = null
) {
    var officeDto: OfficeDto? = null
    var clientDto: ClientDto? = null
}