package com.rafengimprove.currency.currencyrate.service

import com.rafengimprove.currency.currencyrate.model.dto.CurrencyRateDto
import com.rafengimprove.currency.currencyrate.model.dto.toEntity
import com.rafengimprove.currency.currencyrate.model.entity.toDto
import com.rafengimprove.currency.currencyrate.model.enumerated.CurrencyType
import com.rafengimprove.currency.currencyrate.repository.CurrencyRateRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CurrencyRateServiceImpl(private val currencyRateRepository: CurrencyRateRepository) : CurrencyRateService {

    private val log = LoggerFactory.getLogger(CurrencyRateServiceImpl::class.java)

    override fun save(currencyRate: CurrencyRateDto): CurrencyRateDto {
        log.debug("Create a new currency rate")
        if (findByType(currencyRate.type) != null) {
            return editByType(currencyRate.type, currencyRate.rate)!!
        } else {
            return currencyRateRepository.save(currencyRate.toEntity()).toDto()
        }
    }

    override fun editByType(type: CurrencyType, rate: Double): CurrencyRateDto? {
        log.debug("Edit currency rate by type {}", type)
        if (findByType(type) != null) {
            val currencyToUpdate = findByType(type)
            currencyToUpdate?.rate = rate
            return currencyRateRepository.save(currencyToUpdate!!.toEntity()).toDto()
        }
        return null
    }

    override fun findAll(): List<CurrencyRateDto> {
        log.debug("Get all currency rates")
        return currencyRateRepository.findAll().map { it.toDto() }
    }

    override fun findByType(type: CurrencyType): CurrencyRateDto? {
        log.debug("Get currency rate by type {}", type)
        val listOfCurrencyRates = currencyRateRepository.findAll()
        for (element in listOfCurrencyRates) {
            if (element.type == type) {
                return element.toDto()
            }
        }
        return null
    }

    override fun deleteByType(type: CurrencyType): List<CurrencyRateDto> {
        log.debug("Delete currency rate by type {}", type)
        val currencyToDelete = findByType(type)
        currencyToDelete.let {
            currencyRateRepository.delete(currencyToDelete!!.toEntity())
        }
        return findAll()
    }
}