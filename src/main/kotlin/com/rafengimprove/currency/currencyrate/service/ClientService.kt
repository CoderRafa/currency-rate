package com.rafengimprove.currency.currencyrate.service

import com.rafengimprove.currency.currencyrate.model.dto.ClientDto
import com.rafengimprove.currency.currencyrate.model.dto.ClientWithTotalCurrencyDto
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ClientService {
    fun save(clientDto: ClientDto): ClientDto
    fun getClientsAndCombinedSoldCurrencyAmount(type: CurrencyType): List<ClientWithTotalCurrencyDto>
}