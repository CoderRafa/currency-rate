package com.rafengimprove.currency.currencyrate.repository

import com.rafengimprove.currency.currencyrate.model.entity.OfficeEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface OfficeRepository: CrudRepository<OfficeEntity, Long> {

    fun existsByAddressIgnoreCase(address: String): Boolean


    fun findByAddressIgnoreCase(address: String): OfficeEntity


    fun existsByAddressIgnoreCaseAndBankEntity_Id(address: String, id: Long): Boolean


    fun findByAddressIgnoreCaseAndBankEntity_Id(address: String, id: Long): OfficeEntity


    fun findByBankEntity_Id(id: Long): List<OfficeEntity>


}