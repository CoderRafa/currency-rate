package com.rafengimprove.currency.currencyrate.repository

import com.rafengimprove.currency.currencyrate.model.dto.ClientWithTotalCurrencyDto
import com.rafengimprove.currency.currencyrate.model.entity.ClientEntity
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ClientRepository: JpaRepository<ClientEntity, Long> {
//    @Query(
//        """
//        select c.firstName, sum(eo.preExchangeAmount) as total
//from ClientEntity c
//         join c.exchangeOperationEntities eo
//         join eo.officeEntity o
//         join o.bankEntity b
//group by c.id, c.firstName, eo.preExchangeCurrencyType, eo.currencyDirectionType
//having eo.preExchangeCurrencyType = :type
//   and eo.currencyDirectionType = 'SELL'
//order by total desc
//    """
//    )
//    fun getClientAndCombinedCurrencySoldByHim(type: CurrencyType, pageable: Pageable): Page<ClientEntity>

    @Query(
        """
        select c
from ClientEntity c
         join c.exchangeOperationEntities eo
         join eo.officeEntity o
         join o.bankEntity b 
where eo.preExchangeCurrencyType = :type
    """
    )
    fun getClientsAndCombinedCurrencySoldByThem(type: CurrencyType): List<ClientEntity>
}