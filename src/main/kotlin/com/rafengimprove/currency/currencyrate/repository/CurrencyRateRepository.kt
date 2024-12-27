package com.rafengimprove.currency.currencyrate.repository

import com.rafengimprove.currency.currencyrate.model.entity.CurrencyRateEntity
import com.rafengimprove.currency.currencyrate.model.enumerated.CurrencyType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

@Repository
interface CurrencyRateRepository: JpaRepository<CurrencyRateEntity, Long> {

    fun existsByType(type: CurrencyType): Boolean


    fun findByOfficeEntity_CurrencyRateEntities_Type(type: CurrencyType, pageable: Pageable): Page<CurrencyRateEntity>


    fun findAllOfficesByType(type: CurrencyType, pageable: Pageable): Page<CurrencyRateEntity>

}