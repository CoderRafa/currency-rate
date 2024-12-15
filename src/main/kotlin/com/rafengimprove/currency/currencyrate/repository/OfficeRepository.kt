package com.rafengimprove.currency.currencyrate.repository

import com.rafengimprove.currency.currencyrate.model.entity.OfficeEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface OfficeRepository: CrudRepository<OfficeEntity, Long> {

    fun existsByAddressIgnoreCase(address: String): Boolean

    fun findByAddressIgnoreCase(address: String): OfficeEntity

}