package com.rafengimprove.currency.currencyrate.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JSR310Module
import com.ninjasquad.springmockk.MockkBean
import com.rafengimprove.currency.currencyrate.model.dto.CurrencyRateDto
import com.rafengimprove.currency.currencyrate.model.enumerated.CurrencyType
import com.rafengimprove.currency.currencyrate.model.enumerated.CurrencyType.*
import com.rafengimprove.currency.currencyrate.service.CurrencyRateService
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.mockito.Mock
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put

@WebMvcTest(CurrencyRateController::class)
class CurrencyRateControllerTest @Autowired constructor(private val mockMvc: MockMvc) {

    private val log = LoggerFactory.getLogger(CurrencyRateController::class.java)

    @MockkBean
    lateinit var currencyRateService: CurrencyRateService

    private val jsonMapper = ObjectMapper().registerModule(JSR310Module())

    @Test
    fun `Happy pass - create a new currency rate`() {
        val currencyRate = CurrencyRateDto(1, USD, 94.5)
        val currencyCaptor = slot<CurrencyRateDto>()

        every { currencyRateService.save(capture(currencyCaptor)) } returns currencyRate

        mockMvc.post("/api/v1/currency-rate") {
           contentType = MediaType.APPLICATION_JSON
           content = jsonMapper.writeValueAsString(currencyRate)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.id") { value(currencyRate.id)}
        }

        verify(exactly = 1) { currencyRateService.save(any()) }
    }

    @Test
    fun `Happy pass - edit currency rate by type`() {
        val currencyRate = CurrencyRateDto(1, USD, 94.5)
        val currencyCaptor = slot<CurrencyRateDto>()

        every { currencyRateService.save(capture(currencyCaptor)) } returns currencyRate
        every {currencyRateService.editByType(USD, 85.7) } returns currencyRate

        mockMvc.post("/api/v1/currency-rate") {
            contentType = MediaType.APPLICATION_JSON
            content = jsonMapper.writeValueAsString(currencyRate)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.id") { value(currencyRate.id)}
        }

        verify(exactly = 1) { currencyRateService.save(any()) }

        mockMvc.put("/api/v1/currency-rate/USD/85.7") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.id") { value(currencyRate.id)}
            jsonPath("$.rate") {value(currencyRate.rate)}
        }

        verify(exactly = 1) { currencyRateService.save(any()) }

    }

    @Test
    fun `Happy pass - get all currency rates`() {
        val currencyRateEUR = CurrencyRateDto(1, EUR, 80.5)
        val currencyRateUSD = CurrencyRateDto(2, USD, 93.5)
        val currencyRateCNY = CurrencyRateDto(3, CNY, 13.5)
        val currencyRates = listOf(currencyRateEUR, currencyRateUSD, currencyRateCNY)

        every { currencyRateService.save(currencyRateEUR) } returns currencyRateEUR
        every { currencyRateService.save(currencyRateUSD) } returns currencyRateUSD
        every { currencyRateService.save(currencyRateCNY) } returns currencyRateCNY
        every { currencyRateService.findAll() } returns currencyRates

        mockMvc.post("/api/v1/currency-rate") {
            contentType = MediaType.APPLICATION_JSON
            content = jsonMapper.writeValueAsString(currencyRateEUR)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.id") { value(currencyRateEUR.id)}
        }

        mockMvc.post("/api/v1/currency-rate") {
            contentType = MediaType.APPLICATION_JSON
            content = jsonMapper.writeValueAsString(currencyRateUSD)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.id") { value(currencyRateUSD.id)}
        }

        mockMvc.post("/api/v1/currency-rate") {
            contentType = MediaType.APPLICATION_JSON
            content = jsonMapper.writeValueAsString(currencyRateCNY)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.id") { value(currencyRateCNY.id)}
        }

        mockMvc.get("/api/v1/currency-rate") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.size()") { value(currencyRates.size) }
            jsonPath("$[0].id") { value(currencyRateEUR.id) }
        }
    }

    @Test
    fun `Happy pass - get currency rate by type`() {
        val currencyRateEUR = CurrencyRateDto(1, EUR, 80.5)
        val currencyRateUSD = CurrencyRateDto(2, USD, 93.5)

        every { currencyRateService.save(currencyRateEUR) } returns currencyRateEUR
        every { currencyRateService.save(currencyRateUSD) } returns currencyRateUSD
        every { currencyRateService.findByType(EUR) } returns currencyRateEUR

        mockMvc.post("/api/v1/currency-rate") {
            contentType = MediaType.APPLICATION_JSON
            content = jsonMapper.writeValueAsString(currencyRateEUR)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.id") { value(currencyRateEUR.id)}
        }

        mockMvc.post("/api/v1/currency-rate") {
            contentType = MediaType.APPLICATION_JSON
            content = jsonMapper.writeValueAsString(currencyRateUSD)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.id") { value(currencyRateUSD.id)}
        }

        mockMvc.get("/api/v1/currency-rate/EUR") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.id") { value(currencyRateEUR.id)}
        }
    }

    @Test
    fun deleteByType() {
    }

}