package com.rafengimprove.currency.currencyrate.repository

import com.rafengimprove.currency.currencyrate.model.dto.TopTenExchangeOperations
import com.rafengimprove.currency.currencyrate.model.entity.ExchangeOperationEntity
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType
import com.rafengimprove.currency.currencyrate.model.type.OperationType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ExchangeOperationRepository: JpaRepository<ExchangeOperationEntity, Long> {


    fun findByOfficeEntity_Id(id: Long, pageable: Pageable): Page<ExchangeOperationEntity>


    fun findByClientEntity_Id(id: Long, pageable: Pageable): Page<ExchangeOperationEntity>


    @Query(
        """select e from ExchangeOperationEntity e
where e.operationType = ?1 and e.fromCurrencyType = ?2 and e.toCurrencyType = ?3 and e.officeEntity.id = ?4"""
    )
    fun findTopTenByOffice(
        operationType: OperationType,
        fromCurrencyType: CurrencyType,
        toCurrencyType: CurrencyType,
        id: Long,
        pageable: Pageable
    ): List<ExchangeOperationEntity>
}