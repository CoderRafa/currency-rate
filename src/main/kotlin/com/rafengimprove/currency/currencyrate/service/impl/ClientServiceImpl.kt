package com.rafengimprove.currency.currencyrate.service.impl

import com.rafengimprove.currency.currencyrate.model.dto.ClientDto
import com.rafengimprove.currency.currencyrate.model.dto.ClientWithTotalCurrencyDto
import com.rafengimprove.currency.currencyrate.model.dto.toEntity
import com.rafengimprove.currency.currencyrate.model.entity.toDto
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType
import com.rafengimprove.currency.currencyrate.repository.ClientRepository
import com.rafengimprove.currency.currencyrate.service.ClientService
import com.rafengimprove.currency.currencyrate.service.ClientStatsService
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ClientServiceImpl(
    val clientRepository: ClientRepository,
    val clientStatsServiceImpl: ClientStatsService
): ClientService, ClientStatsService by clientStatsServiceImpl  {

    private val log = LoggerFactory.getLogger(ClientServiceImpl::class.java)
    override fun save(clientDto: ClientDto): ClientDto {
        log.debug("Save a new client with name {}", clientDto.firstName)
        return clientRepository.save(clientDto.toEntity()).toDto()
    }

    override fun getClientsAndCombinedSoldCurrencyAmount(type: CurrencyType): List<ClientWithTotalCurrencyDto> {
        log.debug("Get clients with combined currency amount they have sold")
        val client = clientRepository.getClientsAndCombinedCurrencySoldByThem(type).map { it.toDto() }
        val clientList= mutableListOf<ClientWithTotalCurrencyDto>()
        for (element in client) {
            clientList.add(ClientWithTotalCurrencyDto(element.firstName, 100.0))
        }
        return clientList
    }
}