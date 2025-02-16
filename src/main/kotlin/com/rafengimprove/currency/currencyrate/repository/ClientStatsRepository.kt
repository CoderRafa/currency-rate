package com.rafengimprove.currency.currencyrate.repository

import com.rafengimprove.currency.currencyrate.model.entity.ClientStatsEntity
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType
import com.rafengimprove.currency.currencyrate.model.type.OperationType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface ClientStatsRepository : JpaRepository<ClientStatsEntity, Long> {

    fun findByFromCurrencyTypeAndToCurrencyTypeAndOperationTypeAndClientEntity_Id(
        fromCurrencyType: CurrencyType,
        toCurrencyType: CurrencyType,
        operationType: OperationType,
        id: Long
    ): Optional<ClientStatsEntity>


    @Query("""
   select client_entity_id, from_currency_type, to_currency_type, operation_type, total from client_stats_entity
join (select max(total) as max_total
      from client_stats_entity
      where from_currency_type = :fromCurrencyType
        and to_currency_type = :toCurrencyType) max
on total = max.max_total
where operation_type = :operationType
""", nativeQuery = true)
    fun findClientWhoHasMaxTotal(
        fromCurrencyType: CurrencyType,
        toCurrencyType: CurrencyType,
        operationType: OperationType
    ): List<ClientStatsEntity>
}