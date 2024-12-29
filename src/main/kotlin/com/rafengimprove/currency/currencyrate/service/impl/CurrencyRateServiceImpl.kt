package com.rafengimprove.currency.currencyrate.service.impl

import com.rafengimprove.currency.currencyrate.model.dto.CurrencyRateDto
import com.rafengimprove.currency.currencyrate.model.dto.toEntity
import com.rafengimprove.currency.currencyrate.model.entity.toDto
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType
import com.rafengimprove.currency.currencyrate.repository.CurrencyRateRepository
import com.rafengimprove.currency.currencyrate.service.CurrencyRateService
import com.rafengimprove.currency.currencyrate.service.OfficeService
import com.rafengimprove.currency.exception.ElementDoesNotExist
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service


@Service
class CurrencyRateServiceImpl(
    private val currencyRateRepository: CurrencyRateRepository,
    private val officeService: OfficeService
    ) : CurrencyRateService {

    private val log = LoggerFactory.getLogger(CurrencyRateServiceImpl::class.java)

    override fun saveAll(officeId: Long, currencyRates: List<CurrencyRateDto>): List<CurrencyRateDto> {
        log.debug("Create a new currency rate")
        val listOfCurrenciesToReturn = mutableListOf<CurrencyRateDto>()
        val office = officeService.getById(officeId)
        if (office != null) {
            for (currencyRate in currencyRates) {
                if (office.currencyRates.none { it.type == currencyRate.type }) {
                    listOfCurrenciesToReturn.add(currencyRate)
                }
            }
        } else {
            throw ElementDoesNotExist("There is no office with that id")
        }
        return currencyRateRepository.
            saveAll(listOfCurrenciesToReturn
             .map { it.toEntity(office.toEntity(office.bank?.toEntity() ?: throw NoSuchElementException("The problem is here"))) } )
             .map { it.toDto(doINeedOffice = true) }
    }


    override fun editByType(officeId: Long, currencyRateDto: CurrencyRateDto): CurrencyRateDto? {
        log.debug("Edit currency rate by type {}", currencyRateDto.type)
        val office = officeService.getById(officeId)
        val officeToChangeCurrency = officeService.getById(officeId)
        return if (officeToChangeCurrency != null && officeToChangeCurrency.currencyRates.any { it.type == currencyRateDto.type } ) {
            val currencyToUpdate = findByTypeByOffice(officeId, currencyRateDto.type)
            currencyToUpdate?.buyRate = currencyRateDto.buyRate
            currencyToUpdate?.sellRate = currencyRateDto.sellRate
            currencyRateRepository.save(currencyToUpdate!!
                .toEntity(office?.toEntity(office.bank?.toEntity()))).toDto(doINeedCurrencies = false)
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

    override fun findByTypeByOffice(officeId: Long, type: CurrencyType): CurrencyRateDto? {
        log.debug("Get currency rate by type {}", type)
        val officeToShowCurrency = officeService.getById(officeId)
        return if (officeToShowCurrency != null && officeToShowCurrency.currencyRates.any { it.type == type} ) {
            officeService.getById(officeId)?.currencyRates?.first { it.type == type }
        } else {
            throw ElementDoesNotExist("There is no office with that id")
        }
    }

    override fun findByType(type: CurrencyType, pageable: Pageable): Page<CurrencyRateDto> {
      return currencyRateRepository.findAllOfficesByType(type, pageable).map { it.toDto(doINeedOffice = true) }
    }


}