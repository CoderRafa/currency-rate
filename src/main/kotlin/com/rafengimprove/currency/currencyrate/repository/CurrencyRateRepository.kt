package com.rafengimprove.currency.currencyrate.repository

import com.rafengimprove.currency.currencyrate.model.entity.CurrencyRateEntity
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CurrencyRateRepository : JpaRepository<CurrencyRateEntity, Long> {

//    fun existsByType(type: CurrencyType): Boolean
//
//
//    fun findByOfficeEntity_CurrencyRateEntities_Type(type: CurrencyType, pageable: Pageable): Page<CurrencyRateEntity>


//    fun findAllOfficesByType(type: CurrencyType, pageable: Pageable): Page<CurrencyRateEntity>


    @Query(
        """select c from CurrencyRateEntity c
where c.officeEntity.id = ?1 and c.fromCurrencyType = ?2 and c.toCurrencyType = ?3"""
    )
    fun findCurrencyRate(
        id: Long,
        fromCurrencyType: CurrencyType,
        toCurrencyType: CurrencyType
    ): Optional<CurrencyRateEntity>


}