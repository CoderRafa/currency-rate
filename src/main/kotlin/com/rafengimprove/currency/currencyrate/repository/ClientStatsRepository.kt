package com.rafengimprove.currency.currencyrate.repository

import com.rafengimprove.currency.currencyrate.model.entity.ClientStatsEntity
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType
import com.rafengimprove.currency.currencyrate.model.type.OperationType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface ClientStatsRepository : JpaRepository<ClientStatsEntity, Long> {

    fun findByFromCurrencyTypeAndToCurrencyTypeAndOperationTypeAndClientEntity_Id(
        fromCurrencyType: CurrencyType,
        toCurrencyType: CurrencyType,
        operationType: OperationType,
        id: Long
    ): Optional<ClientStatsEntity>


    @Query("""
    SELECT c FROM ClientStatsEntity c
    WHERE c.fromCurrencyType = :from
      AND c.toCurrencyType = :to
      AND c.total = (
          SELECT MAX(c2.total) FROM ClientStatsEntity c2
          WHERE c2.fromCurrencyType = :from
            AND c2.toCurrencyType = :to
      )
      AND c.operationType = :operation
""")
    fun findClientWhoHasMaxTotal(
        @Param("from") fromCurrencyType: CurrencyType,
        @Param("to") toCurrencyType: CurrencyType,
        @Param("operation") operationType: OperationType
    ): List<ClientStatsEntity>
}