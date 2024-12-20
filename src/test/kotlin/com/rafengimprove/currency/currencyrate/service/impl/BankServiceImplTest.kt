package com.rafengimprove.currency.currencyrate.service.impl

import com.rafengimprove.currency.currencyrate.model.dto.BankDto
import com.rafengimprove.currency.currencyrate.service.BankService
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("h2")
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@SpringBootTest
class BankServiceImplTest @Autowired constructor(val bankService: BankService) {

    @Test
    fun `Happy pass - save a new bank`() {
        val newBank = bankService.save(BankDto(1, "Rafa", "Very successful bank" ))

        assertEquals("Rafa", newBank.name)
        assertEquals(1, newBank.id)
    }

    @Test
    fun editByName() {
    }

    @Test
    fun getByName() {
    }

    @Test
    fun getAll() {
    }

    @Test
    fun getById() {
    }

    @Test
    fun getBankRepository() {
    }
}