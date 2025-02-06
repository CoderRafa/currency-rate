package com.rafengimprove.currency.currencyrate.repository

import com.rafengimprove.currency.currencyrate.model.entity.ClientStatsEntity
import com.rafengimprove.currency.currencyrate.model.type.OperationType
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ClientStatsRepository : JpaRepository<ClientStatsEntity, Long> {

    fun findByFromCurrencyTypeAndToCurrencyTypeAndOperationTypeAndClientEntity_Id(
        fromCurrencyType: CurrencyType,
        toCurrencyType: CurrencyType,
        operationType: OperationType,
        id: Long
    ): Optional<ClientStatsEntity>



}