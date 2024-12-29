package com.rafengimprove.currency.currencyrate.service

import com.rafengimprove.currency.currencyrate.model.dto.BankDto


interface BankService {
    fun save(bank: BankDto): BankDto
    fun editByName(name: String, bank: BankDto): BankDto?
    fun getByName(name: String): BankDto?
    fun getAll(): List<BankDto>
}