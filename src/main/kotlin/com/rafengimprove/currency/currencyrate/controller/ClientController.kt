package com.rafengimprove.currency.currencyrate.controller

import com.rafengimprove.currency.currencyrate.model.dto.ClientDto
import com.rafengimprove.currency.currencyrate.model.dto.ClientWithTotalCurrencyDto
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType
import com.rafengimprove.currency.currencyrate.service.ClientService
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*

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

    @GetMapping("/combined/sold")
    fun getCombinedSoldCurrency(
        @RequestParam type: CurrencyType,
        @PageableDefault(size = 10, page = 0) pageable: Pageable
    ): List<ClientWithTotalCurrencyDto> {
        log.debug("Get clients and the combined amount of currency {} the sold", type)
        return clientService.getClientsAndCombinedSoldCurrencyAmount(type)
    }

    @DeleteMapping("/{id}")
    fun deleteClientById(
        @PathVariable("id") id: Long
    ) {
        return clientService.deleteClientById(id)
    }
}