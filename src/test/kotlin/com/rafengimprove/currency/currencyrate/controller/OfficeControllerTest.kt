package com.rafengimprove.currency.currencyrate.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JSR310Module
import com.ninjasquad.springmockk.MockkBean
import com.rafengimprove.currency.currencyrate.model.dto.OfficeDto
import com.rafengimprove.currency.currencyrate.service.OfficeService
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

@WebMvcTest(OfficeController::class)
class OfficeControllerTest @Autowired constructor(private val mockMvc: MockMvc) {

    @MockkBean
    lateinit var officeService: OfficeService

    private val jasonMapper = ObjectMapper().registerModule(JSR310Module())

    @Test
    fun `Happy pass - create a new office`() {
        val newOffice = listOf(OfficeDto(1, "First st. 44", "Cool office", 250.0))
        val officeCaptor = slot<List<OfficeDto>>()

        every { officeService.save(1, capture(officeCaptor)) } returns newOffice

        mockMvc.post("/bank/1/office") {
            contentType = MediaType.APPLICATION_JSON
            content = jasonMapper.writeValueAsString(newOffice)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }

    }
}