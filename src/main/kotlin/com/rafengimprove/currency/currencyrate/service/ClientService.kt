package com.rafengimprove.currency.currencyrate.service

import com.rafengimprove.currency.currencyrate.model.dto.ClientDto
import com.rafengimprove.currency.currencyrate.model.dto.ClientWithTotalCurrencyDto
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType
import com.rafengimprove.currency.currencyrate.model.type.SortType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime

interface ClientService {
    fun save(clientDto: ClientDto): ClientDto

    fun getClientsAndCombinedSoldCurrencyAmount(type: CurrencyType): List<ClientWithTotalCurrencyDto>

    fun deleteClientById(id: Long)

    fun findById(id: Long, doINeedExchangeOperations: Boolean = true): ClientDto

    fun findByPartialName(partialName: String, pageable: Pageable): Page<ClientDto>

    fun getClientsSortedByDateAndTimeCreated(sortType: SortType, pageable: Pageable): Page<ClientDto>

    fun getClientsSortedByLastname(sortType: SortType, pageable: Pageable): Page<ClientDto>

    fun getClientsByPartialNameInPeriod(partialName: String, date: LocalDateTime, pageable: Pageable): Page<ClientDto>
}