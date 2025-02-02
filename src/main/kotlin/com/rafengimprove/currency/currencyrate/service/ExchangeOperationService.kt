package com.rafengimprove.currency.currencyrate.service

import com.rafengimprove.currency.currencyrate.model.dto.ExchangeDataDto

interface ExchangeOperationService {
//   fun add(exchangeOperationDto: ExchangeDataDto)
    fun exchange(exchangeDataDto: ExchangeDataDto): Boolean

    fun deleteById(id: Long)
}