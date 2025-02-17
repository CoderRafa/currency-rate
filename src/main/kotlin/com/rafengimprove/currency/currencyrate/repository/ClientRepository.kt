package com.rafengimprove.currency.currencyrate.repository

import com.rafengimprove.currency.currencyrate.model.dto.ClientWithTotalCurrencyDto
import com.rafengimprove.currency.currencyrate.model.entity.ClientEntity
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType
import com.rafengimprove.currency.currencyrate.model.type.OperationType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ClientRepository: JpaRepository<ClientEntity, Long> {

    @Query(
        """
        select c
from ClientEntity c
         join c.exchangeOperationEntities eo
         join eo.officeEntity o
         join o.bankEntity b 
where eo.toCurrencyType = :type and eo.operationType = 'SELL'
    """
    )
    fun getClientsAndCombinedCurrencySoldByThem(type: CurrencyType): List<ClientEntity>
}