package com.rafengimprove.currency.currencyrate.repository

import com.rafengimprove.currency.currencyrate.model.entity.BankEntity
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface BankRepository : JpaRepository<BankEntity, Long> {

    fun existsByNameIgnoreCase(name: String): Boolean


    @Query("select b from BankEntity b where upper(b.name) = upper(?1)", nativeQuery = false)
    fun findByName(name: String): BankEntity?

    @Query(
        """
        select b from BankEntity b
        join fetch b.officeEntities o
        join fetch o.currencyRateEntities cr
        where cr.fromCurrencyType = :type or cr.toCurrencyType = :type
    """
    )
    fun findBanksWorkingWithType(type: CurrencyType, pageable: Pageable): Page<BankEntity>
}