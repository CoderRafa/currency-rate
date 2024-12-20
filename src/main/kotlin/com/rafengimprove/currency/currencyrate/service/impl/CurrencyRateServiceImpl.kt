package com.rafengimprove.currency.currencyrate.service.impl

import com.rafengimprove.currency.currencyrate.model.dto.CurrencyRateDto
import com.rafengimprove.currency.currencyrate.model.dto.toEntity
import com.rafengimprove.currency.currencyrate.model.entity.toDto
import com.rafengimprove.currency.currencyrate.model.enumerated.CurrencyType
import com.rafengimprove.currency.currencyrate.repository.CurrencyRateRepository
import com.rafengimprove.currency.currencyrate.service.CurrencyRateService
import com.rafengimprove.currency.currencyrate.service.OfficeService
import com.rafengimprove.currency.exception.ElementDoesNotExist
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CurrencyRateServiceImpl(
    private val currencyRateRepository: CurrencyRateRepository,
    private val officeService: OfficeService
    ) : CurrencyRateService {

    private val log = LoggerFactory.getLogger(CurrencyRateServiceImpl::class.java)

    override fun saveAll(officeId: Long, currencyRates: List<CurrencyRateDto>): List<CurrencyRateDto> {
        log.debug("Create a new currency rate")
        return if (officeService.getById(officeId)?.currencyRates?.isEmpty() ?: throw ElementDoesNotExist("There is no office with that id")) {
            val currencyRateEntities = currencyRates.map { it.toEntity(officeService.getById(officeId)?.toEntity()) }
            currencyRateRepository.saveAll(currencyRateEntities)
            currencyRateEntities.map { it.toDto(doINeedCurrencies = false) }
        } else {
            findAll(officeId)
        }
    }

    override fun editByType(officeId: Long, currencyRateDto: CurrencyRateDto): CurrencyRateDto? {
        log.debug("Edit currency rate by type {}", currencyRateDto.type)
        val officeToChangeCurrency = officeService.getById(officeId)
        return if (officeToChangeCurrency != null && officeToChangeCurrency.currencyRates.any { it.type == currencyRateDto.type } ) {
            val currencyToUpdate = findByType(officeId, currencyRateDto.type)
            currencyToUpdate?.rate = currencyRateDto.rate
            currencyRateRepository.save(currencyToUpdate!!.toEntity()).toDto()
        } else {
            throw ElementDoesNotExist("There is no office with that id or it doesn't have that currency type")
        }
    }

    override fun findAll(officeId: Long): List<CurrencyRateDto> {
        log.debug("Get all currency rates")
        return if (officeService.getById(officeId) != null) {
            officeService.getById(officeId)?.currencyRates!!.toList()
        } else {
            throw ElementDoesNotExist("There is no office with that id")
        }
    }

    override fun findByType(officeId: Long, type: CurrencyType): CurrencyRateDto? {
        log.debug("Get currency rate by type {}", type)
        val officeToShowCurrency = officeService.getById(officeId)
        return if (officeToShowCurrency != null && officeToShowCurrency.currencyRates.any { it.type == type} ) {
            officeService.getById(officeId)?.currencyRates?.first { it.type == type }
        } else {
            throw ElementDoesNotExist("There is no office with that id")
        }
    }
}