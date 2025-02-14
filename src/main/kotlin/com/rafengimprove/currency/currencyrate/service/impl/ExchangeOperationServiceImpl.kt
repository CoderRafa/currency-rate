package com.rafengimprove.currency.currencyrate.service.impl

import com.rafengimprove.currency.currencyrate.exception.ElementDoesNotExist
import com.rafengimprove.currency.currencyrate.model.dto.ExchangeDataDto
import com.rafengimprove.currency.currencyrate.model.dto.ExchangeOperationDto
import com.rafengimprove.currency.currencyrate.model.dto.toEntity
import com.rafengimprove.currency.currencyrate.model.entity.toDto
import com.rafengimprove.currency.currencyrate.model.type.OperationType
import com.rafengimprove.currency.currencyrate.repository.ClientRepository
import com.rafengimprove.currency.currencyrate.repository.CurrencyRateRepository
import com.rafengimprove.currency.currencyrate.repository.ExchangeOperationRepository
import com.rafengimprove.currency.currencyrate.repository.OfficeRepository
import com.rafengimprove.currency.currencyrate.service.ExchangeOperationService
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import kotlin.math.pow
import kotlin.math.round

@Service
class ExchangeOperationServiceImpl(
    val clientRepository: ClientRepository,
    val officeRepository: OfficeRepository,
    val exchangeOperationRepository: ExchangeOperationRepository,
    val clientStatsServiceImpl: ClientStatsServiceImpl,
    val currencyRateRepository: CurrencyRateRepository,
    val clientServiceImpl: ClientServiceImpl // TODO: Убрать если не нужно
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
        exchangeEntity.clientEntity = clientRepository.findById(exchangeDataDto.clientId).orElseThrow() // TODO: Нужно сделать кастомные ошибки с понятными текстами
        exchangeEntity.officeEntity = officeRepository.findById(exchangeDataDto.officeId).orElseThrow()

        return with(exchangeDataDto) {
            currencyRateRepository.findCurrencyRate(
                this.officeId,
                this.fromCurrencyType,
                this.toCurrencyType
            )
        }.map {
            exchangeEntity.receiveAmount = when (exchangeDataDto.operationType) {
                OperationType.SELL -> it.buyRate.times(exchangeDataDto.giveAmount)
                OperationType.BUY -> exchangeDataDto.giveAmount.div(it.sellRate).roundToDecimalPlaces(2)
            }
        }.map {
            exchangeOperationRepository.save(exchangeEntity)
        }.map {
            clientStatsServiceImpl.modifyClientStats(exchangeDataDto)
            it.id != null
        }.orElse(false)
    }

    override fun getAll(): List<ExchangeOperationDto> {
        return exchangeOperationRepository.findAll().map { it.toDto() }
    }

    override fun getById(id: Long): ExchangeOperationDto {
        return exchangeOperationRepository.findById(id).orElseThrow().toDto()
    }

    override fun deleteById(id: Long) {
        exchangeOperationRepository.deleteById(id)
    }

    override fun getByOffice(id: Long, pageable: Pageable): Page<ExchangeOperationDto> {
        return exchangeOperationRepository.findByOfficeEntity_Id(id, pageable).map { it.toDto() }
    }

    override fun getByClient(id: Long, pageable: Pageable): Page<ExchangeOperationDto> {
        return exchangeOperationRepository.findByClientEntity_Id(id, pageable).map { it.toDto() }
    }

//    override fun add(exchangeOperationDto: ExchangeDataDto) { // TODO: Убрать если не нужно
//        val exchangeOperation = exchangeOperationRepository.save(exchangeOperationDto.toEntity()).toDto()
//        clientServiceImpl.modifyClientStats(exchangeOperationDto)
//    }
}

fun Double.roundToDecimalPlaces(decimalPlaces: Int): Double {
    val factor = 10.0.pow(decimalPlaces)
    return round(this * factor) / factor
}