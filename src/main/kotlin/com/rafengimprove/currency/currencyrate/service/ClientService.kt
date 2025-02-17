package com.rafengimprove.currency.currencyrate.service

import com.rafengimprove.currency.currencyrate.model.dto.ClientDto
import com.rafengimprove.currency.currencyrate.model.dto.ClientWithTotalCurrencyDto
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType

interface ClientService {
    fun save(clientDto: ClientDto): ClientDto

    fun getClientsAndCombinedSoldCurrencyAmount(type: CurrencyType): List<ClientWithTotalCurrencyDto>

    fun deleteClientById(id: Long)
    fun findById(id: Long, doINeedExchangeOperations: Boolean = true): ClientDto
}