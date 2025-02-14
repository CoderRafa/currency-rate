package com.rafengimprove.currency.currencyrate.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.jayway.jsonpath.JsonPath
import com.rafengimprove.currency.currencyrate.model.dto.BankDto
import org.hamcrest.Matchers
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.*

@ActiveProfiles("h2")
@SpringBootTest
@AutoConfigureMockMvc
class BankControllerTest @Autowired constructor(private val mockMvc: MockMvc) {

    val mapper = ObjectMapper()

    @Test
    fun `Happy pass - create a new bank`() {

        val newBank = BankDto(null, "Rafa", "Cool bank")

        mockMvc.post("/api/v1/bank") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(newBank)
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

        val savedBank = mockMvc.post("/api/v1/bank") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(newBank)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.name") { value(newBank.name) }
        }.andReturn().response.contentAsString

        mockMvc.get("/api/v1/bank") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.size()", Matchers.`is`(1))
        }

        val id: Long = JsonPath.read(savedBank, "$.id")

        mockMvc.delete("/api/v1/bank/$id") {
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
    fun `Negative pass - bank name is blank`() {
        val newBank = BankDto(null, "", "Cool bank")

        mockMvc.post("/api/v1/bank") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(newBank)
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `Negative pass - bank name is a number`() {
        val invalidJson = """{ "name": 12345, "description": "Cool bank" }"""

        mockMvc.post("/api/v1/bank") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(invalidJson)
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `Negative pass - get a bank which is not present in the database`() {

        mockMvc.get("/api/v1/bank/phantom") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isNotFound() }
        }.andReturn().response.contentAsString
    }

    @Test
    fun `Negative pass - delete a non existent bank`() {

        mockMvc.delete("/api/v1/bank/1") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
        }
    }

    @Test
    fun `Negative pass - edit non existing bank`() {
        val updatedNonExistingBank = BankDto(null, "Phantom2", "Can't be added")

        mockMvc.put("/api/v1/bank/phantom") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(updatedNonExistingBank)
        }.andExpect {
            status { isNotFound() }
        }
    }
}