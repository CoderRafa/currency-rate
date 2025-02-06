package com.rafengimprove.currency.currencyrate.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.jayway.jsonpath.JsonPath
import com.rafengimprove.currency.currencyrate.model.dto.BankDto
import com.rafengimprove.currency.currencyrate.model.dto.OfficeDto
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
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
class OfficeControllerTest @Autowired constructor(private val mockMvc: MockMvc) {

    @Test
    fun `Happy pass - create a new office`() {
        val newBank = BankDto(null, "Rafa", "Cool bank")
        val newOffice = listOf(OfficeDto(null, "First st. 44", "Cool office", 250.0))

        val savedBankResponse = mockMvc.post("/api/v1/bank") {
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(newBank)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.name") { value(newBank.name) }
        }.andReturn().response.contentAsString

        val id: Long = JsonPath.read(savedBankResponse, "$.id")

        mockMvc.post("/api/v1/bank/$id/office") {
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(newOffice)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }
    }

    @Test
    fun `Happy pass - delete an office`() {
        val newBank = BankDto(null, "Rafa", "Cool bank")
        val newOffice = listOf(OfficeDto(null, "First st. 44", "Cool office", 250.0))

        val savedBankResponse = mockMvc.post("/api/v1/bank") {
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(newBank)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.name") { value(newBank.name) }
        }.andReturn().response.contentAsString

        val id: Long = JsonPath.read(savedBankResponse, "$.id")

        val savedOffice = mockMvc.post("/api/v1/bank/$id/office") {
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(newOffice)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }.andReturn().response.contentAsString

        val officeIdToDelete: Long = JsonPath.read(savedOffice, "$[0].id")

        val gotOffice = mockMvc.get("/api/v1//office/$officeIdToDelete") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.id") { value(officeIdToDelete)}
        }.andReturn().response.contentAsString

//        println("Office is: $officeIdToDelete")

        mockMvc.delete("/api/v1//office/$officeIdToDelete") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
        }

        val gotOfficeAfterDeleting = mockMvc.get("/api/v1//office/$officeIdToDelete") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isNotFound() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }.andReturn().response.contentAsString
    }
}