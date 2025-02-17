package com.rafengimprove.currency.currencyrate.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JSR310Module
import com.fasterxml.jackson.datatype.jsr310.deser.JSR310DateTimeDeserializerBase
import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.jayway.jsonpath.JsonPath
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
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@ActiveProfiles("h2")
@SpringBootTest
@AutoConfigureMockMvc
class ClientControllerTest @Autowired constructor(val mockMvc: MockMvc) {

    @Test
    fun `Happy pass - create a new client`() {

        val newClient = ClientDto(null, "Vilhelm", "Bay", "FG458794", "bay@gmail.com")

        mockMvc.post("/api/v1/client") {
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(newClient)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.firstName") { value(newClient.firstName) }
        }
    }

    @Test
    fun `Happy pass - delete a client`() {
        val newClient = ClientDto(null, "Vilhelm", "Bay", "FG458794", "bay@gmail.com")

        val savedClient = mockMvc.post("/api/v1/client") {
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(newClient)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.firstName") { value(newClient.firstName) }
        }.andReturn().response.contentAsString

        val savedClientId: Long = JsonPath.read(savedClient, "$.id")

        mockMvc.get("/api/v1/client/$savedClientId") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            jsonPath("$.firstName") {value(newClient.firstName)}
        }

        mockMvc.delete("/api/v1/client/$savedClientId") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
        }

        mockMvc.get("/api/v1/client/$savedClientId") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isNotFound() }
        }
    }
}