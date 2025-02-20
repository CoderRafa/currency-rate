package com.rafengimprove.currency.currencyrate.model.dto

import com.rafengimprove.currency.currencyrate.model.type.CurrencyType
import com.rafengimprove.currency.currencyrate.model.type.OperationType

data class TopTenExchangeOperations(
    val fromCurrencyType: CurrencyType,
    val toCurrencyType: CurrencyType,
    val operationType: OperationType,
    val officeId: Long,
    val clientId: Long,
    val amount: Double
)
