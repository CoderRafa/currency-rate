package com.rafengimprove.currency.currencyrate.service

import com.rafengimprove.currency.currencyrate.model.dto.BankDto
import com.rafengimprove.currency.currencyrate.model.entity.BankEntity


interface BankService {
    fun save(bank: BankDto): BankDto
    fun editByName(name: String, bank: BankDto): BankDto?
    fun getByName(name: String): BankDto?
    fun getById(id: Long): BankEntity
    fun getAll(): List<BankDto>
}