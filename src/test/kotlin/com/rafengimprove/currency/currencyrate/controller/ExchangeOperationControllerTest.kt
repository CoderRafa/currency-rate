package com.rafengimprove.currency.currencyrate.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JSR310Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.jayway.jsonpath.JsonPath
import com.rafengimprove.currency.currencyrate.model.dto.*
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType.*
import com.rafengimprove.currency.currencyrate.model.type.OperationType.*
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@ActiveProfiles("h2")
@SpringBootTest
@AutoConfigureMockMvc
class ExchangeOperationControllerTest @Autowired constructor(private val mockMvc: MockMvc) {

    @Test
    fun `Happy pass - create exchange operation`() {
        val newBank = BankDto(null, "Rafa", "Cool bank")
        val newOffice = listOf(OfficeDto(null, "First st. 44", "Cool office", 250.0))
        val newCurrencyRate = listOf(CurrencyRateDto(null, RUB, USD, 98.5, 102.5))
        val newClient = ClientDto(null, "Vilhelm", "Bay", "FG458794", "bay@gmail.com")

        val savedBankResponse = mockMvc.post("/api/v1/bank") {
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(newBank)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.name") { value(newBank.name) }
        }.andReturn().response.contentAsString

        val savedBankId: Long = JsonPath.read(savedBankResponse, "$.id")

        val savedOfficeResponse = mockMvc.post("/api/v1/bank/$savedBankId/office") {
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(newOffice)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }.andReturn().response.contentAsString

        val savedOfficeId: Long = JsonPath.read(savedOfficeResponse, "$[0].id")

        mockMvc.post("/api/v1/office/$savedOfficeId/currency-rates") {
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(newCurrencyRate)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$[0].sellRate") { value(newCurrencyRate[0].sellRate) }
        }

        val savedClient = mockMvc.post("/api/v1/client") {
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(newClient)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.firstName") { value(newClient.firstName) }
        }.andReturn().response.contentAsString

        val savedClientId: Long = JsonPath.read(savedClient, "$.id")

//        val dateTimeString = "2025-02-08 19:37:43.052162"
//        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")
//        val dateTime = LocalDateTime.parse(dateTimeString, formatter)

        val newExchangeData = ExchangeDataDto(
            savedOfficeId, savedClientId, 100.0, SELL, RUB, USD, 9850.0, LocalDateTime.now()
        )

        val mapper = ObjectMapper().apply {
            registerModule(JavaTimeModule())
        }

        mockMvc.post("/api/v1/exchange") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(newExchangeData)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }

        mockMvc.get("/api/v1/exchange") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.size()", Matchers.`is` (1))
        }
    }
}