package com.rafengimprove.currency.currencyrate.service.impl

import com.rafengimprove.currency.currencyrate.model.dto.BankDto
import com.rafengimprove.currency.currencyrate.model.dto.toEntity
import com.rafengimprove.currency.currencyrate.model.entity.BankEntity
import com.rafengimprove.currency.currencyrate.model.entity.toDto
import com.rafengimprove.currency.currencyrate.repository.BankRepository
import com.rafengimprove.currency.currencyrate.repository.OfficeRepository
import com.rafengimprove.currency.currencyrate.service.BankService
import com.rafengimprove.currency.currencyrate.service.OfficeService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class BankServiceImpl(val bankRepository: BankRepository) : BankService {

    private val log = LoggerFactory.getLogger(BankServiceImpl::class.java)
    override fun save(bank: BankDto): BankDto {
        log.debug("Save a new bank with name {}", bank.name)
        return if (bankRepository.existsByNameIgnoreCase(bank.name)) {
            bankRepository.findByName(bank.name)!!
        } else {
            bankRepository.save(bank.toEntity())
        }.toDto(doINeedOffices = false)
    }

    override fun editByName(name: String, bank: BankDto): BankDto? {
        log.debug("Edit a bank with name {}", name)
        return if (bankRepository.existsByNameIgnoreCase(name)) {
            val bankToUpdate = getByName(name)
            bankToUpdate?.name = bank.name
            bankToUpdate?.description = bank.description
            bankToUpdate?.offices = bank.offices.map { it.toEntity().toDto(doINeedOffices = false) }.toMutableSet()
            bankRepository.save(bankToUpdate!!.toEntity()).toDto(doINeedOffices = false)
        } else {
            null
        }
    }

    override fun getByName(name: String): BankDto? {
        log.debug("Get a bank with name {}", name)
        return bankRepository.findByName(name)?.toDto(doINeedOffices = true)
    }

    override fun getAll(): List<BankDto> {
        log.debug("Get all banks")
       return bankRepository.findAll().map { it.toDto(doINeedOffices = true) }
    }

    override fun getById(id: Long): BankEntity {
        log.debug("Get bank by id {}", id)
        return bankRepository.findById(id).orElse(null)
    }
}




