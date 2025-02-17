package com.rafengimprove.currency.currencyrate.repository

import com.rafengimprove.currency.currencyrate.model.entity.ExchangeOperationEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface ExchangeOperationRepository: JpaRepository<ExchangeOperationEntity, Long> {


    fun findByOfficeEntity_Id(id: Long, pageable: Pageable): Page<ExchangeOperationEntity>


    fun findByClientEntity_Id(id: Long, pageable: Pageable): Page<ExchangeOperationEntity>


}