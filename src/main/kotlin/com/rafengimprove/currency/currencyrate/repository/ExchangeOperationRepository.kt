package com.rafengimprove.currency.currencyrate.repository

import com.rafengimprove.currency.currencyrate.model.entity.ExchangeOperationEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ExchangeOperationRepository: JpaRepository<ExchangeOperationEntity, Long>