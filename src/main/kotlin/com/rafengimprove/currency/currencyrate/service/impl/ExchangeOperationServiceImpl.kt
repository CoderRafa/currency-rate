package com.rafengimprove.currency.currencyrate.service.impl

import com.rafengimprove.currency.currencyrate.model.dto.ExchangeDataDto
import com.rafengimprove.currency.currencyrate.model.dto.toEntity
import com.rafengimprove.currency.currencyrate.model.entity.toDto
import com.rafengimprove.currency.currencyrate.model.type.OperationType
import com.rafengimprove.currency.currencyrate.repository.ClientRepository
import com.rafengimprove.currency.currencyrate.repository.CurrencyRateRepository
import com.rafengimprove.currency.currencyrate.repository.ExchangeOperationRepository
import com.rafengimprove.currency.currencyrate.repository.OfficeRepository
import com.rafengimprove.currency.currencyrate.service.ExchangeOperationService
import com.rafengimprove.currency.currencyrate.service.OfficeService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ExchangeOperationServiceImpl(
    val clientRepository: ClientRepository,
    val officeRepository: OfficeRepository,
    val exchangeOperationRepository: ExchangeOperationRepository,
    val clientStatsServiceImpl: ClientStatsServiceImpl,
    val currencyRateRepository: CurrencyRateRepository,
    val clientServiceImpl: ClientServiceImpl
) : ExchangeOperationService {
    private val log = LoggerFactory.getLogger(ExchangeOperationServiceImpl::class.java)

    override fun exchange(exchangeDataDto: ExchangeDataDto): Boolean {
        log.debug(
            "Exchange operation by client id: {} from {} to {}",
            exchangeDataDto.clientId,
            exchangeDataDto.fromCurrencyType,
            exchangeDataDto.toCurrencyType
        )
        val exchangeEntity = exchangeDataDto.toEntity()
        exchangeEntity.clientEntity = clientRepository.findById(exchangeDataDto.clientId).orElseThrow()
        exchangeEntity.officeEntity = officeRepository.findById(exchangeDataDto.officeId).orElseThrow()

        return with(exchangeDataDto) {
            currencyRateRepository.findCurrencyRate(
                this.officeId,
                this.fromCurrencyType,
                this.toCurrencyType
            )
        }.map {
            exchangeEntity.receiveAmount = when(exchangeDataDto.operationType) {
                OperationType.SELL -> it.buyRate.times(exchangeDataDto.giveAmount)
                OperationType.BUY -> it.sellRate.times(exchangeDataDto.giveAmount)
            }
        }.map {
            exchangeOperationRepository.save(exchangeEntity)
        }.map {
            clientStatsServiceImpl.modifyClientStats(exchangeDataDto)
            it.id != null
        }.orElse(false)
    }

    override fun deleteById(id: Long) {
        if (exchangeOperationRepository.findById(id) != null) {
            exchangeOperationRepository.deleteById(id)
        }
    }
//    override fun add(exchangeOperationDto: ExchangeDataDto) {
//        val exchangeOperation = exchangeOperationRepository.save(exchangeOperationDto.toEntity()).toDto()
//        clientServiceImpl.modifyClientStats(exchangeOperationDto)
//    }
}