package com.rafengimprove.currency.currencyrate.model.event

import com.rafengimprove.currency.currencyrate.model.dto.ExchangeDataDto
import org.springframework.context.ApplicationEvent

class ModifyClientStatsEvent(source: Any, val exchangeDataDto: ExchangeDataDto): ApplicationEvent(source)