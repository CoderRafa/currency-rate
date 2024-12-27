package com.rafengimprove.currency.currencyrate.repository

import com.rafengimprove.currency.currencyrate.model.entity.BankEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface BankRepository: JpaRepository<BankEntity, Long> {

    fun existsByNameIgnoreCase(name: String): Boolean


    @Query("select b from BankEntity b where upper(b.name) = upper(?1)")
    fun findByName(name: String): BankEntity?
}