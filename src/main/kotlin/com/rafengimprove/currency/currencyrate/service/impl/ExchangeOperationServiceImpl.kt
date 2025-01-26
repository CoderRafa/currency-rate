package com.rafengimprove.currency.currencyrate.service.impl

import com.rafengimprove.currency.currencyrate.model.dto.ExchangeDataDto
import com.rafengimprove.currency.currencyrate.model.dto.ExchangeOperationDto
import com.rafengimprove.currency.currencyrate.model.dto.toEntity
import com.rafengimprove.currency.currencyrate.model.entity.toDto
import com.rafengimprove.currency.currencyrate.repository.ExchangeOperationRepository
import com.rafengimprove.currency.currencyrate.service.ExchangeOperationService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ExchangeOperationServiceImpl(
    val clientServiceImpl: ClientServiceImpl,


    val exchangeOperationRepository: ExchangeOperationRepository
) : ExchangeOperationService {
    private val log = LoggerFactory.getLogger(ExchangeOperationServiceImpl::class.java)

    override fun exchange(exchangeDataDto: ExchangeDataDto): Boolean {
        log.debug(
            "Exchange operation by client id: {} from {} to {}",
            exchangeDataDto.clientId,
            exchangeDataDto.preExchangeCurrencyType,
            exchangeDataDto.postExchangeCurrencyType
        )
        val exchangeEntity = exchangeDataDto.toEntity()
        exchangeEntity.clientEntity = clientServiceImpl.findBy(exchangeDataDto.clientId)
        exchangeEntity.officeEntity = officeService.findBy(exchangeDataDto.officeId)

        return exchangeOperationRepository.save(exchangeEntity).also {  }.id != null
    }

    override fun add(exchangeOperationDto: ExchangeDataDto) {
        val exchangeOperation = exchangeOperationRepository.save(exchangeOperationDto.toEntity()).toDto()
        clientServiceImpl.modifyClientStats(exchangeOperation)
    }
}