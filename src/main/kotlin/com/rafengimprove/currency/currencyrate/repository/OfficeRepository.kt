package com.rafengimprove.currency.currencyrate.repository

import com.rafengimprove.currency.currencyrate.model.entity.OfficeEntity
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface OfficeRepository: JpaRepository<OfficeEntity, Long> {

    fun existsByAddressIgnoreCase(address: String): Boolean


    fun findByAddressIgnoreCase(address: String): OfficeEntity


    fun existsByAddressIgnoreCaseAndBankEntity_Id(address: String, id: Long): Boolean


    fun findByAddressIgnoreCaseAndBankEntity_Id(address: String, id: Long): OfficeEntity


    fun findByBankEntity_Id(id: Long): List<OfficeEntity>


    fun findByCurrencyRateEntities_TypeOrderByCurrencyRateEntities_BuyRateDesc(
        type: CurrencyType,
        pageable: Pageable
    ): Page<OfficeEntity>


    fun findByCurrencyRateEntities_TypeOrderByCurrencyRateEntities_SellRateAsc(
        type: CurrencyType,
        pageable: Pageable
    ): Page<OfficeEntity>

    @Query("""
        select o from OfficeEntity o 
        join fetch o.currencyRateEntities cr
        where cr.type = :type
    """)
    fun findOfficesWorkingWithType(type: CurrencyType, pageable: Pageable): Page<OfficeEntity>
}