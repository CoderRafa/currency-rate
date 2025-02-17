package com.rafengimprove.currency.currencyrate.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.jayway.jsonpath.JsonPath
import com.rafengimprove.currency.currencyrate.model.dto.BankDto
import com.rafengimprove.currency.currencyrate.model.dto.CurrencyRateDto
import com.rafengimprove.currency.currencyrate.model.dto.OfficeDto
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType.*
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

@ActiveProfiles("h2")
@SpringBootTest
@AutoConfigureMockMvc
class CurrencyRateControllerTest @Autowired constructor(private val mockMvc: MockMvc) {

    @Test
    fun `Happy pass - create a new currency rate`() {
        val newBank = BankDto(null, "Rafa", "Cool bank")
        val newOffice = listOf(OfficeDto(null, "First st. 44", "Cool office", 250.0))
        val newCurrencyRate = listOf(CurrencyRateDto(null, RUB, USD, 98.5, 102.5))

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

        mockMvc.get("/api/v1/office/$savedOfficeId/currency-rates") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.size()", Matchers.`is`(1))
        }
    }

    @Test
    fun `Happy pass - delete currency rate`() {
        val newBank = BankDto(null, "Rafa", "Cool bank")
        val newOffice = listOf(OfficeDto(null, "First st. 44", "Cool office", 250.0))
        val newCurrencyRate = listOf(CurrencyRateDto(null, RUB, USD, 98.5, 102.5))

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

        val savedCurrencyRate = mockMvc.post("/api/v1/office/$savedOfficeId/currency-rates") {
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(newCurrencyRate)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$[0].sellRate") { value(newCurrencyRate[0].sellRate) }
        }.andReturn().response.contentAsString

        val savedCurrencyRateId: Long = JsonPath.read(savedCurrencyRate, "$[0].id")

        mockMvc.get("/api/v1/office/$savedOfficeId/currency-rates") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.size()", Matchers.`is`(1))
        }

        mockMvc.delete("/api/v1/office/delete-currency-rate/$savedCurrencyRateId") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
        }

        mockMvc.get("/api/v1/office/$savedOfficeId/currency-rates") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.size()", Matchers.`is`(0))
        }
    }
}