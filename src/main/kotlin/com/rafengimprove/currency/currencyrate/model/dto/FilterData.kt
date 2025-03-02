package com.rafengimprove.currency.currencyrate.model.dto

import java.time.LocalDateTime

data class FilterData(val text: String? = null, val beginDate: LocalDateTime? = null, val endDate: LocalDateTime? = null)