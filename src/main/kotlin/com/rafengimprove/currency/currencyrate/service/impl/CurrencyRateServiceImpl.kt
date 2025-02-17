package com.rafengimprove.currency.currencyrate.service.impl

import com.rafengimprove.currency.currencyrate.exception.ElementDoesNotExist
import com.rafengimprove.currency.currencyrate.model.dto.CurrencyRateDto
import com.rafengimprove.currency.currencyrate.model.dto.toEntity
import com.rafengimprove.currency.currencyrate.model.entity.toDto
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType
import com.rafengimprove.currency.currencyrate.repository.CurrencyRateRepository
import com.rafengimprove.currency.currencyrate.service.CurrencyRateService
import com.rafengimprove.currency.currencyrate.service.OfficeService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service


@Service
class CurrencyRateServiceImpl(
    private val currencyRateRepository: CurrencyRateRepository,
    private val officeService: OfficeService,
) : CurrencyRateService {


    private val log = LoggerFactory.getLogger(CurrencyRateServiceImpl::class.java)

    override fun saveAll(officeId: Long, currencyRates: List<CurrencyRateDto>): List<CurrencyRateDto> {
        log.debug("Create a new currency rate")
        val listOfCurrenciesToReturn = mutableListOf<CurrencyRateDto>()
        val office = officeService.getById(officeId)
        if (office != null) {
            for (currencyRate in currencyRates) {
                if (office.currencyRates.none { it.fromCurrencyType == currencyRate.fromCurrencyType && it.toCurrencyType == currencyRate.toCurrencyType }) {
                    listOfCurrenciesToReturn.add(currencyRate)
                }
            }
        } else {
            throw ElementDoesNotExist("There is no office with that id")
        }
        try {
            return currencyRateRepository.saveAll(listOfCurrenciesToReturn
                .map {
                    it.toEntity(
                        office.toEntity(
                            office.bank?.toEntity() ?: throw NoSuchElementException("The problem is here")
                        )
                    )
                })
                .map { it.toDto(doINeedOffice = true) }
        } catch (e: Exception) {
            log.error("Error saving currency rates", e)  // Logs full stack trace
            throw e  // Optionally rethrow the exception or handle it
        }
    }


    override fun editByType(officeId: Long, currencyRateDto: CurrencyRateDto): CurrencyRateDto? {
        log.debug(
            "Edit currency rate by from currency type {} and to currency type {}",
            currencyRateDto.fromCurrencyType,
            currencyRateDto.toCurrencyType
        )
       officeService.getById(officeId)
        val officeToChangeCurrency = officeService.getById(officeId)
        return if (officeToChangeCurrency != null && officeToChangeCurrency.currencyRates.any { it.fromCurrencyType == currencyRateDto.fromCurrencyType && it.toCurrencyType == currencyRateDto.toCurrencyType }) {
            val currencyToUpdate = findBy(officeId, currencyRateDto.fromCurrencyType, currencyRateDto.toCurrencyType)
            currencyToUpdate?.buyRate = currencyRateDto.buyRate
            currencyToUpdate?.sellRate = currencyRateDto.sellRate
            currencyRateRepository.save(
                currencyToUpdate!!
                    .toEntity(/*office?.toEntity(office.bank?.toEntity())*/) // TODO: Убрать если не нужно
            ).toDto(doINeedCurrencies = false)
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

    override fun findBy(
        officeId: Long,
        fromCurrencyType: CurrencyType,
        toCurrencyType: CurrencyType
    ): CurrencyRateDto? {
        log.debug(
            "Get currency rate by from currency type {} and to currency type {}",
            fromCurrencyType,
            toCurrencyType
        )
        val officeToShowCurrency = officeService.getById(officeId)
        return if (officeToShowCurrency != null && officeToShowCurrency.currencyRates.any { it.fromCurrencyType == fromCurrencyType && it.toCurrencyType == toCurrencyType }) {
            officeService.getById(officeId)?.currencyRates?.first { it.fromCurrencyType == fromCurrencyType && it.toCurrencyType == toCurrencyType }
        } else {
            throw ElementDoesNotExist("There is no office with that criteria")
        }
    }

    override fun deleteCurrencyRateById(id: Long) {

        currencyRateRepository.deleteById(id)
    }

//    override fun findBy(type: CurrencyType, pageable: Pageable): Page<CurrencyRateDto> {
//      return currencyRateRepository.findAllOfficesByType(type, pageable).map { it.toDto(doINeedOffice = true) }
//    }


}