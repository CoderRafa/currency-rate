package com.rafengimprove.currency.currencyrate.controller

import com.fasterxml.jackson.databind.ObjectMapper
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
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.time.LocalDateTime

@ActiveProfiles("h2")
@SpringBootTest
@AutoConfigureMockMvc
class ExchangeOperationControllerTest @Autowired constructor(private val mockMvc: MockMvc) {

    val mapper = ObjectMapper().apply {
        registerModule(JavaTimeModule())
    }
    
    @Test
    fun `Happy pass - create exchange operation`() {
        val newBank = BankDto(null, "Rafa", "Cool bank")
        val newOffice = listOf(OfficeDto(null, "First st. 44", "Cool office", 250.0))
        val newCurrencyRate = listOf(CurrencyRateDto(null, RUB, USD, 98.5, 102.5))
        val newClient = ClientDto(null, "Vilhelm", "Bay", "FG458794", "bay@gmail.com")

        val bankJsonString = mapper.writeValueAsString(newBank)
        mapper.readTree(bankJsonString)

        val savedBankResponse = mockMvc.post("/api/v1/bank") {
            contentType = MediaType.APPLICATION_JSON
            content = bankJsonString
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.name") { value(newBank.name) }
        }.andReturn().response.contentAsString

        val savedBankId: Long = JsonPath.read(savedBankResponse, "$.id")


        val officeJsonString = mapper.writeValueAsString(newOffice)
        mapper.readTree(officeJsonString)

        val savedOfficeResponse = mockMvc.post("/api/v1/bank/$savedBankId/office") {
            contentType = MediaType.APPLICATION_JSON
            content = officeJsonString
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }.andReturn().response.contentAsString

        val savedOfficeId: Long = JsonPath.read(savedOfficeResponse, "$[0].id")

        mockMvc.post("/api/v1/office/$savedOfficeId/currency-rates") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(newCurrencyRate)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$[0].sellRate") { value(newCurrencyRate[0].sellRate) }
        }

        val clientJsonString = mapper.writeValueAsString(newClient)
        mapper.readTree(clientJsonString)

        val savedClient = mockMvc.post("/api/v1/client") {
            contentType = MediaType.APPLICATION_JSON
            content = clientJsonString
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.firstName") { value(newClient.firstName) }
        }.andReturn().response.contentAsString

        val savedClientId: Long = JsonPath.read(savedClient, "$.id")

        val newExchangeData = ExchangeDataDto(
            savedOfficeId, savedClientId, 100.0, SELL, RUB, USD, 9850.0, LocalDateTime.now()
        )
        
        val exchangeOperationJsonString = mapper.writeValueAsString(newExchangeData)

        mapper.readTree(exchangeOperationJsonString)
//        require(exchangeOperationJsonNode.isObject) { "Invalid JSON format" }
        
        mockMvc.post("/api/v1/exchange") {
            contentType = MediaType.APPLICATION_JSON
            content = exchangeOperationJsonString
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

    @Test
    fun `Happy pass - delete an exchange operation`() {
        val newBank = BankDto(null, "Rafa", "Cool bank")
        val newOffice = listOf(OfficeDto(null, "First st. 44", "Cool office", 250.0))
        val newCurrencyRate = listOf(CurrencyRateDto(null, RUB, USD, 98.5, 102.5))
        val newClient = ClientDto(null, "Vilhelm", "Bay", "FG458794", "bay@gmail.com")

        val bankJsonString = mapper.writeValueAsString(newBank)
        mapper.readTree(bankJsonString)

        val savedBankResponse = mockMvc.post("/api/v1/bank") {
            contentType = MediaType.APPLICATION_JSON
            content = bankJsonString
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.name") { value(newBank.name) }
        }.andReturn().response.contentAsString

        val savedBankId: Long = JsonPath.read(savedBankResponse, "$.id")


        val officeJsonString = mapper.writeValueAsString(newOffice)
        mapper.readTree(officeJsonString)

        val savedOfficeResponse = mockMvc.post("/api/v1/bank/$savedBankId/office") {
            contentType = MediaType.APPLICATION_JSON
            content = officeJsonString
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }.andReturn().response.contentAsString

        val savedOfficeId: Long = JsonPath.read(savedOfficeResponse, "$[0].id")

        mockMvc.post("/api/v1/office/$savedOfficeId/currency-rates") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(newCurrencyRate)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$[0].sellRate") { value(newCurrencyRate[0].sellRate) }
        }

        val clientJsonString = mapper.writeValueAsString(newClient)
        mapper.readTree(clientJsonString)

        val savedClient = mockMvc.post("/api/v1/client") {
            contentType = MediaType.APPLICATION_JSON
            content = clientJsonString
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.firstName") { value(newClient.firstName) }
        }.andReturn().response.contentAsString

        val savedClientId: Long = JsonPath.read(savedClient, "$.id")

        val newExchangeData = ExchangeDataDto(
            savedOfficeId, savedClientId, 100.0, SELL, RUB, USD, 9850.0, LocalDateTime.now()
        )

        val exchangeOperationJsonString = mapper.writeValueAsString(newExchangeData)

        mapper.readTree(exchangeOperationJsonString)
//        require(exchangeOperationJsonNode.isObject) { "Invalid JSON format" }

        val savedExchangeOperation = mockMvc.post("/api/v1/exchange") {
            contentType = MediaType.APPLICATION_JSON
            content = exchangeOperationJsonString
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }.andReturn().response.contentAsString

        mockMvc.get("/api/v1/exchange") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.size()", Matchers.`is` (1))
        }

        val savedExchangeOperationId: Long = JsonPath.read(savedExchangeOperation, "$.id")

        mockMvc.delete("/api/v1/exchange/$savedExchangeOperationId") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
        }

        mockMvc.get("/api/v1/exchange") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.size()", Matchers.`is` (0))
        }
    }
}