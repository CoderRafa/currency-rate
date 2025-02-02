package com.rafengimprove.currency.currencyrate.model.dto

import com.rafengimprove.currency.currencyrate.model.entity.ExchangeOperationEntity
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType
import com.rafengimprove.currency.currencyrate.model.type.OperationType
import java.time.LocalDateTime

data class ExchangeDataDto(
    val officeId: Long,
    val clientId: Long,
    val giveAmount: Double,
    val operationType: OperationType,
    val fromCurrencyType: CurrencyType = CurrencyType.USD,
    val toCurrencyType: CurrencyType = CurrencyType.RUB,
    var receiveAmount: Double = 0.0,
    val dateTime: LocalDateTime = LocalDateTime.now()
)

fun ExchangeDataDto.toEntity() = ExchangeOperationEntity(
    operationType,
    giveAmount,
    fromCurrencyType,
    toCurrencyType,
    dateTime
)