package com.rafengimprove.currency.currencyrate.service.impl

import com.rafengimprove.currency.currencyrate.exception.ElementDoesNotExist
import com.rafengimprove.currency.currencyrate.model.dto.BankDto
import com.rafengimprove.currency.currencyrate.model.dto.toEntity
import com.rafengimprove.currency.currencyrate.model.entity.toDto
import com.rafengimprove.currency.currencyrate.repository.BankRepository
import com.rafengimprove.currency.currencyrate.service.BankService
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
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
            getByName(name)
                ?.apply { updateFields(bank, this) }
                ?.let { bankRepository.save(it.toEntity()) }
                ?.toDto(doINeedOffices = false)
        } else {
            throw ElementDoesNotExist("A bank with that name doesn't exist")
        }
    }

    private fun updateFields(from: BankDto, to: BankDto) {
        to.name = from.name
        to.description = from.description
        to.offices = from.offices.map { it.toEntity().toDto(doINeedOffices = false) }.toMutableSet()
    }

    override fun getByName(name: String): BankDto? {
        log.debug("Get a bank with name {}", name)
        return bankRepository.findByName(name)?.toDto(doINeedOffices = true) ?: throw ElementDoesNotExist("There is no bank with that name")
    }

    override fun getAll(): List<BankDto> {
        log.debug("Get all banks")
        return bankRepository.findAll().map { it.toDto(doINeedOffices = true, doINeedCurrencies = true) }
    }

    override fun getAllByCurrency(currencyType: CurrencyType, pageable: Pageable): Page<BankDto> {
        return bankRepository.findBanksWorkingWithType(currencyType, pageable)
            .map { it.toDto() }
    }

    override fun deleteById(id: Long) {
            bankRepository.deleteById(id)
    }

//    override fun getAllBanksThatDoNotWorkWithCurrency(currencyType: CurrencyType, pageable: Pageable): Page<BankDto> { // TODO: Убираем
//        return bankRepository.findBanksNotWorkingWithCurrency(currencyType, pageable).map { it.toDto() }
//    }
}




