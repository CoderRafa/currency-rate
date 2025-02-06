package com.rafengimprove.currency.currencyrate.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JSR310Module
import com.ninjasquad.springmockk.MockkBean
import com.rafengimprove.currency.currencyrate.model.dto.BankDto
import com.rafengimprove.currency.currencyrate.service.BankService
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.hamcrest.Matchers
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
class BankControllerTest @Autowired constructor(private val mockMvc: MockMvc) {

    @Test
    fun `Happy pass - create a new bank`() {
        val newBank = BankDto(null, "Rafa", "Cool bank")

        mockMvc.post("/api/v1/bank") {
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(newBank)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.name") { value(newBank.name) }
        }

        mockMvc.get("/api/v1/bank") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.size()", Matchers.`is`(1))
        }
    }


    @Test
    fun `Happy pass - delete a bank`() {
        val newBank = BankDto(null, "Rafa", "Cool bank")

        mockMvc.post("/api/v1/bank") {
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(newBank)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.name") { value(newBank.name) }
        }

        mockMvc.get("/api/v1/bank") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.size()", Matchers.`is`(1))
        }

        mockMvc.delete("/api/v1/bank/Rafa") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
        }

        mockMvc.get("/api/v1/bank") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.size()", Matchers.`is`(0))
        }
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