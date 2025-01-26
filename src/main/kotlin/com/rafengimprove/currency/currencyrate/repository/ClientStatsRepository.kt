package com.rafengimprove.currency.currencyrate.repository

import com.rafengimprove.currency.currencyrate.model.entity.ClientStatsEntity
import com.rafengimprove.currency.currencyrate.model.type.CurrencyDirectionType
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType
import org.springframework.data.jpa.repository.JpaRepository

interface ClientStatsRepository : JpaRepository<ClientStatsEntity, Long> {


    fun findByClientIdAndCurrencyTypeAndCurrencyDirectionType(
        id: Long,
        currencyType: CurrencyType,
        currencyDirectionType: CurrencyDirectionType
    ): ClientStatsEntity


}