package com.rafengimprove.currency.currencyrate.service.impl

import com.rafengimprove.currency.currencyrate.model.dto.BankDto
import com.rafengimprove.currency.currencyrate.service.BankService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.EnableTransactionManagement

@ActiveProfiles("h2")
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@SpringBootTest(classes = [LiquibaseTestConfiguration::class])
class BankServiceImplTest @Autowired constructor(val bankService: BankService) {

    @Test
    fun `Happy pass - save a new bank`() {
        val newBank = bankService.save(BankDto(null, "Rafa", "Very successful bank" ))

        assertEquals("Rafa", newBank.name)
        Assertions.assertThat(newBank).hasNoNullFieldsOrProperties()
    }

    @Test
    fun `Happy pass - delete a bank`() {
        val banksBeforeAddingAnotherOne = bankService.getAll()

//        val newBank = bankService.save(BankDto(1, "BankyTheBank", "Imaginary bank"))
//
//        assertThat(bankService.getAll().size - banksBeforeAddingAnotherOne.size).isEqualTo(1)
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

@TestConfiguration
@ComponentScan("com.rafengimprove.currency.currencyrate")
@EnableAutoConfiguration
@EnableTransactionManagement
@EntityScan("com.rafengimprove.currency.currencyrate.model.entity")
@EnableJpaRepositories(basePackages = ["com.rafengimprove.currency.currencyrate.repository"])
class LiquibaseTestConfiguration