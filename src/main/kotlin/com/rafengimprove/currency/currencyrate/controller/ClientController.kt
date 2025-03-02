package com.rafengimprove.currency.currencyrate.controller

import com.rafengimprove.currency.currencyrate.model.dto.ClientDto
import com.rafengimprove.currency.currencyrate.model.dto.ClientFiltersContainer
import com.rafengimprove.currency.currencyrate.model.dto.ClientWithTotalCurrencyDto
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType
import com.rafengimprove.currency.currencyrate.model.type.SortType
import com.rafengimprove.currency.currencyrate.model.type.SortType.ASC
import com.rafengimprove.currency.currencyrate.service.ClientService
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/v1/client")
class ClientController(val clientService: ClientService) {

    private val log = LoggerFactory.getLogger(ClientController::class.java)

    @PostMapping
    fun save(@RequestBody clientDto: ClientDto): ClientDto {
        log.debug("Save a new client")
        return clientService.save(clientDto)
    }

    @GetMapping("/{id}")
    fun getClientById(@PathVariable("id") id: Long): ClientDto {
        return clientService.findById(id)
    }

    @GetMapping("/sorted-by-created")
    fun getClientsSortedByDateAndTimeCreated(
        @RequestParam sortType: SortType = ASC,
        @PageableDefault(size = 10, page = 0) pageable: Pageable
    ): Page<ClientDto> {
        return clientService.getClientsSortedByDateAndTimeCreated(sortType, pageable)
    }

    @GetMapping("/sorted-by-lastname")
    fun getClientsSortedByLastname(
        @RequestParam sortType: SortType = ASC,
        @PageableDefault(size = 10, page = 0) pageable: Pageable
    ): Page<ClientDto> {
        return clientService.getClientsSortedByLastname(sortType, pageable)
    }

    @GetMapping("/combined/sold")
    fun getCombinedSoldCurrency(
        @RequestParam type: CurrencyType,
        @PageableDefault(size = 10, page = 0) pageable: Pageable
    ): List<ClientWithTotalCurrencyDto> {
        log.debug("Get clients and the combined amount of currency {} they sold", type)
        return clientService.getClientsAndCombinedSoldCurrencyAmount(type)
    }

    @GetMapping("/partial-name")
    fun getByPartialName(
        @RequestParam partialName: String,
        @PageableDefault(size = 10, page = 0) pageable: Pageable
    ): Page<ClientDto> {
        return clientService.findByPartialName(partialName, pageable)
    }

    @GetMapping("/partial-name/period")
    fun getByPartialNameInPeriod(
        @RequestParam partialName: String,
        @RequestParam date: LocalDateTime = LocalDateTime.now(),
        @PageableDefault(size = 10, page = 0) pageable: Pageable
    ): Page<ClientDto> {
       return clientService.getClientsByPartialNameInPeriod(partialName, date, pageable)
    }


    @DeleteMapping("/{id}")
    fun deleteClientById(
        @PathVariable("id") id: Long
    ) {
        return clientService.deleteClientById(id)
    }

    @GetMapping
    fun getAll(clientFiltersContainer: ClientFiltersContainer, pageable: Pageable): Page<ClientDto> {
        log.info("Start to find all client by filters and sorts: {}", clientFiltersContainer)
        return clientService.findAll(clientFiltersContainer, pageable)
    }
}