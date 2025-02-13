package com.rafengimprove.currency.currencyrate.service

import com.rafengimprove.currency.currencyrate.model.dto.ExchangeDataDto
import com.rafengimprove.currency.currencyrate.model.dto.ExchangeOperationDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ExchangeOperationService {
//   fun add(exchangeOperationDto: ExchangeDataDto)
    fun exchange(exchangeDataDto: ExchangeDataDto): Boolean

    fun getAll(): List<ExchangeOperationDto>

    fun getById(id: Long): ExchangeOperationDto

    fun deleteById(id: Long)

    fun getByOffice(id: Long, pageable: Pageable): Page<ExchangeOperationDto>

    fun getByClient(id: Long, pageable: Pageable): Page<ExchangeOperationDto>

}