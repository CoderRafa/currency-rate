package com.rafengimprove.currency.currencyrate.service.impl

import com.rafengimprove.currency.currencyrate.exception.ElementDoesNotExist
import com.rafengimprove.currency.currencyrate.model.dto.ClientDto
import com.rafengimprove.currency.currencyrate.model.dto.ClientFiltersContainer
import com.rafengimprove.currency.currencyrate.model.dto.ClientWithTotalCurrencyDto
import com.rafengimprove.currency.currencyrate.model.dto.toEntity
import com.rafengimprove.currency.currencyrate.model.entity.toDto
import com.rafengimprove.currency.currencyrate.model.type.ClientSortFieldType
import com.rafengimprove.currency.currencyrate.model.type.ClientSortFieldType.*
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType
import com.rafengimprove.currency.currencyrate.model.type.SortType
import com.rafengimprove.currency.currencyrate.model.type.SortType.ASC
import com.rafengimprove.currency.currencyrate.model.type.SortType.DESC
import com.rafengimprove.currency.currencyrate.repository.ClientRepository
import com.rafengimprove.currency.currencyrate.service.ClientService
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ClientServiceImpl(
    val clientRepository: ClientRepository,
) : ClientService
{

    private val log = LoggerFactory.getLogger(ClientServiceImpl::class.java)

    override fun save(clientDto: ClientDto): ClientDto {
        log.debug("Save a new client with name {}", clientDto.firstName)
        return clientRepository.save(clientDto.toEntity()).toDto()
    }

    override fun findById(id: Long, doINeedExchangeOperations: Boolean): ClientDto {
        return clientRepository
            .findById(id)
            .map { it.toDto(doINeedExchangeOperations = doINeedExchangeOperations) }
            .orElseThrow { ElementDoesNotExist("This client does not exist") }
    }

    override fun findByPartialName(partialName: String, pageable: Pageable): Page<ClientDto> {
        return clientRepository.findByPartialName(partialName, pageable).map { it.toDto() }
    }

    override fun getClientsSortedByDateAndTimeCreated(sortType: SortType, pageable: Pageable): Page<ClientDto> {
        return when (sortType) {
            ASC -> clientRepository.getAllClientsSortedByDateAndTimeCreatedAsc(pageable)
                .map { it.toDto(doINeedExchangeOperations = false) }

            DESC -> clientRepository.getAllClientsSortedByDateAndTimeCreatedDesc(pageable)
                .map { it.toDto(doINeedExchangeOperations = false) }
        }
    }

    override fun getClientsSortedByLastname(sortType: SortType, pageable: Pageable): Page<ClientDto> {
        return if (sortType == ASC) {
            clientRepository.getAllClientsSortedByLastnameAsc(pageable).map { it.toDto(doINeedExchangeOperations = false) }
        } else {
            clientRepository.getAllClientsSortedByLastnameDesc(pageable).map { it.toDto(doINeedExchangeOperations = false) }
        }
    }

    override fun getClientsByPartialNameInPeriod(
        partialName: String,
        date: LocalDateTime,
        pageable: Pageable
    ): Page<ClientDto> {
        return clientRepository.getClientsByPartialNameCreatedInPeriod(partialName, date.minusMonths(1), pageable)
            .map { it.toDto(doINeedExchangeOperations = false) }
    }

    override fun getClientsAndCombinedSoldCurrencyAmount(type: CurrencyType): List<ClientWithTotalCurrencyDto> {
        log.debug("Get clients with combined currency amount they have sold")
        return clientRepository
            .getClientsAndCombinedCurrencySoldByThem(type)
            .map { ClientWithTotalCurrencyDto(it.firstName, 100.0) }
    }

    override fun deleteClientById(id: Long) {
        clientRepository.deleteById(id)
    }

    override fun findAll(clientFiltersContainer: ClientFiltersContainer, pageable: Pageable): Page<ClientDto> {
        if (clientFiltersContainer.clientSort != null) {
            when {
                clientFiltersContainer.clientSort.field == LAST_NAME and clientFiltersContainer.clientSort.sortType == ASC -> clientRepository.getAllClientsSortedByLastnameAsc(
                    pageable
                )
            }
        }
    }