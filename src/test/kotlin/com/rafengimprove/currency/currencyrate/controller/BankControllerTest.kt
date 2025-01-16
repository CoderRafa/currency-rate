package com.rafengimprove.currency.currencyrate.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JSR310Module
import com.ninjasquad.springmockk.MockkBean
import com.rafengimprove.currency.currencyrate.model.dto.BankDto
import com.rafengimprove.currency.currencyrate.service.BankService
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@WebMvcTest(BankControllerTest::class)
class BankControllerTest @Autowired constructor(private val mockMvc: MockMvc) {

    @MockkBean
    lateinit var bankService: BankService

    private val jsonMapper = ObjectMapper().registerModule(JSR310Module())


    @Test
    fun `Happy pass - create a new bank`() {
        val newBank = BankDto(1, "Rafa", "Cool bank")
        val bankCaptor = slot<BankDto>()

        every { bankService.save(capture(bankCaptor)) } returns newBank

        mockMvc.post("/api/v1/bank") {
            contentType = MediaType.APPLICATION_JSON
            content = jsonMapper.writeValueAsString(newBank)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.id") { value(newBank.id) }
        }

        verify(exactly = 1) { bankService.save(any()) }
    }


    @Test
    fun getAllBanks() {
    }

    @Test
    fun getBankByName() {
    }

    @Test
    fun editBankByName() {
    }

    @Test
    fun getBankService() {
    }
}