package com.rafengimprove.currency.currencyrate.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JSR310Module
import com.fasterxml.jackson.datatype.jsr310.deser.JSR310DateTimeDeserializerBase
import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.ninjasquad.springmockk.MockkBean
import com.rafengimprove.currency.currencyrate.model.dto.ClientDto
import com.rafengimprove.currency.currencyrate.service.ClientService
import io.mockk.every
import io.mockk.slot
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@WebMvcTest(ClientController ::class)
class ClientControllerTest @Autowired constructor(val mockMvc: MockMvc) {

    @MockkBean
    lateinit var clientService: ClientService

    private val jsonMapper = ObjectMapper().registerModule(JSR310Module())

    @Test
    fun `Happy pass - create a new client`() {

        val newClient = ClientDto(1, "Vilhelm", "Bay", "FG458794", "bay@gmail.com")
        val clientCaptor = slot<ClientDto>()

        every { clientService.save(capture(clientCaptor)) } returns newClient

        mockMvc.post("/api/v1/client") {
            contentType = MediaType.APPLICATION_JSON
            content = jsonMapper.writeValueAsString(newClient)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.id") { value(newClient.id) }
        }
    }
}