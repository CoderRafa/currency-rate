package com.rafengimprove.currency.currencyrate.service

import com.rafengimprove.currency.currencyrate.model.dto.CurrencyRateDto
import com.rafengimprove.currency.currencyrate.model.enumerated.CurrencyType.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource

@ActiveProfiles("h2")
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@SpringBootTest
class CurrencyRateServiceImplTest @Autowired constructor(val currencyRateService: CurrencyRateService) {

    private val log = LoggerFactory.getLogger(CurrencyRateServiceImplTest::class.java)

    @Test
    @Order(0)
    fun `Happy pass - save an account`() {

        val savedCurrencyRate = currencyRateService.save(CurrencyRateDto(1, USD, 75.5))

        log.debug("Saved currency rate {}", savedCurrencyRate)

        assertEquals(USD, savedCurrencyRate.type)
        assertEquals(75.5, savedCurrencyRate.rate)
    }

    @Test
    @Order(5)
    fun `Happy pass - edit a currency rate by type`() {
        val savedCurrencyRate = currencyRateService.save(CurrencyRateDto(1, USD, 90.5))
        log.debug("Saved currency rate {}", savedCurrencyRate)

        assertEquals(1, savedCurrencyRate.id)
        assertEquals(USD, savedCurrencyRate.type)
        assertEquals(90.5, savedCurrencyRate.rate)

        val editedCurrencyRate = currencyRateService.editByType(USD, 93.0)

        assertEquals(1, editedCurrencyRate?.id)
        assertEquals(USD, editedCurrencyRate?.type)
        assertEquals(93.0, editedCurrencyRate?.rate)
    }

    @Test
    fun findAll() {
    }

    @Test
    fun findByType() {
    }

    @Test
    fun deleteByType() {
    }
}
