package com.rafengimprove.currency.currencyrate.service

import com.rafengimprove.currency.currencyrate.model.dto.BankDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType


interface BankService {
    fun save(bank: BankDto): BankDto
    fun editByName(name: String, bank: BankDto): BankDto?
    fun getByName(name: String): BankDto?
    fun getAll(): List<BankDto>
    fun getAllByCurrency(currencyType: CurrencyType, pageable: Pageable): Page<BankDto>
    fun deleteBankByName(name: String)
//    fun getAllBanksThatDoNotWorkWithCurrency(currencyType: CurrencyType, pageable: Pageable): Page<BankDto>
}