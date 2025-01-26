package com.rafengimprove.currency.currencyrate.service

import com.rafengimprove.currency.currencyrate.model.dto.ExchangeOperationDto

interface ExchangeOperationService {
   fun save(exchangeOperationDto: ExchangeOperationDto): ExchangeOperationDto?
}