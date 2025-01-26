package com.rafengimprove.currency.currencyrate.model.dto

import com.rafengimprove.currency.currencyrate.model.entity.ExchangeOperationEntity
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType
import com.rafengimprove.currency.currencyrate.model.type.OperationType
import java.time.LocalDateTime

data class ExchangeDataDto(
    val officeId: Long,
    val clientId: Long,
    val amount: Double,
    val operationType: OperationType,
    val preExchangeCurrencyType: CurrencyType = CurrencyType.USD,
    val postExchangeCurrencyType: CurrencyType = CurrencyType.RUB,
    var postExchangeAmount: Double = 0.0,
)

fun ExchangeDataDto.toEntity() = ExchangeOperationEntity(
    operationType,
    amount,
    preExchangeCurrencyType,
    postExchangeCurrencyType,
    LocalDateTime.now()
)