package com.rafengimprove.currency.currencyrate.repository

import com.rafengimprove.currency.currencyrate.model.entity.ClientEntity
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

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

    @Query("select c from ClientEntity c where c.firstName like %?1% or c.lastName like %?1%")
    fun findByPartialName(partialName: String, pageable: Pageable): Page<ClientEntity>

    @Query("select c from ClientEntity c where c.dateAndTimeCreated is not null order by c.dateAndTimeCreated")
    fun getAllClientsSortedByDateAndTimeCreatedAsc(pageable: Pageable): Page<ClientEntity>

    @Query("select c from ClientEntity c where c.dateAndTimeCreated is not null order by c.dateAndTimeCreated DESC")
    fun getAllClientsSortedByDateAndTimeCreatedDesc(pageable: Pageable): Page<ClientEntity>

    @Query("select c from ClientEntity c where c.lastName is not null order by c.lastName")
    fun getAllClientsSortedByLastnameAsc(pageable: Pageable): Page<ClientEntity>

    @Query("select c from ClientEntity c where c.lastName is not null order by c.lastName DESC")
    fun getAllClientsSortedByLastnameDesc(pageable: Pageable): Page<ClientEntity>

    @Query("select c from ClientEntity c where c.firstName like %?1% or c.lastName like %?1% and c.dateAndTimeCreated >= ?2")
    fun getClientsByPartialNameCreatedInPeriod(partialName: String, date: LocalDateTime, pageable: Pageable): Page<ClientEntity>

}