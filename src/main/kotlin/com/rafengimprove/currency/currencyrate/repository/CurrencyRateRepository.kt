package com.rafengimprove.currency.currencyrate.repository

import com.rafengimprove.currency.currencyrate.model.entity.CurrencyRateEntity
import com.rafengimprove.currency.currencyrate.model.enumerated.CurrencyType
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CurrencyRateRepository: CrudRepository<CurrencyRateEntity, Long> {

    fun existsByType(type: CurrencyType): Boolean

    @Query("select c from CurrencyRateEntity c where c.type = ?1")
    fun findByType(type: CurrencyType): CurrencyRateEntity

}