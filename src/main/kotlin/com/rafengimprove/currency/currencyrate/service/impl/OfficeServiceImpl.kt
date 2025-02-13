package com.rafengimprove.currency.currencyrate.service.impl

import com.rafengimprove.currency.currencyrate.exception.ElementDoesNotExist
import com.rafengimprove.currency.currencyrate.model.dto.OfficeDto
import com.rafengimprove.currency.currencyrate.model.dto.toEntity
import com.rafengimprove.currency.currencyrate.model.entity.OfficeEntity
import com.rafengimprove.currency.currencyrate.model.entity.toDto
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType
import com.rafengimprove.currency.currencyrate.model.type.OperationType
import com.rafengimprove.currency.currencyrate.model.type.OperationType.BUY
import com.rafengimprove.currency.currencyrate.model.type.OperationType.SELL
import com.rafengimprove.currency.currencyrate.repository.BankRepository
import com.rafengimprove.currency.currencyrate.repository.OfficeRepository
import com.rafengimprove.currency.currencyrate.service.OfficeService
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class OfficeServiceImpl(val officeRepository: OfficeRepository, val bankRepository: BankRepository) : OfficeService {

    private val log = LoggerFactory.getLogger(OfficeServiceImpl::class.java)

    override fun save(bankId: Long, offices: List<OfficeDto>): List<OfficeDto> {
        val officesList = mutableListOf<OfficeDto>()
        for (office in offices) {
            if (officeRepository.existsByAddressIgnoreCaseAndBankEntity_Id(office.address, bankId)) {
                officesList.add(
                    officeRepository.findByAddressIgnoreCaseAndBankEntity_Id(office.address, bankId)
                        .toDto(doINeedOffices = false)
                )
            } else {
                officesList.add(
                    officeRepository.save(office.toEntity(bankRepository.findById(bankId).get()))
                        .toDto(doINeedOffices = false, doINeedBank = true)
                )
            }
        }
        return officesList
    }

    override fun editById(bankId: Long, office: OfficeDto): OfficeDto? {
        return if (officeRepository.existsById(office.id!!)) {
            val officeToUpdate = getById(office.id)
            officeToUpdate?.address = office.address
            officeToUpdate?.description = office.description
            officeToUpdate?.area = office.area
            officeToUpdate?.bank = office.bank
            officeToUpdate?.currencyRates = office.currencyRates.map { it.toEntity().toDto() }.toMutableSet()
            officeRepository.save(officeToUpdate!!.toEntity(bankRepository.findById(bankId).get())).toDto()
        } else {
            null
        }
    }

    override fun getById(officeId: Long): OfficeDto? {
        return if (officeRepository.existsById(officeId)) {
            officeRepository.findById(officeId).map { it.toDto(doINeedOffices = false) }.orElseThrow()
        } else {
            throw ElementDoesNotExist("The office does not exist")
        }
    }

    override fun getAllByBank(bankId: Long): List<OfficeDto> {
        return officeRepository.findByBankEntity_Id(bankId).map { it.toDto(doINeedBank = false) }
    }

    override fun findOfficesBy(
        currencyType: CurrencyType,
        operationType: OperationType,
        pageable: Pageable
    ): Page<OfficeDto> {
        log.debug("Find rates by the type: {} and direction: {}", currencyType, operationType)
        return when (operationType) {
            BUY -> officeRepository.findByCurrencyRateEntities_ToCurrencyTypeOrderByCurrencyRateEntities_SellRateAsc(
                currencyType,
                pageable
            )

            SELL -> officeRepository.findByCurrencyRateEntities_ToCurrencyTypeOrderByCurrencyRateEntities_BuyRateDesc(
                currencyType,
                pageable
            )
        }.map(OfficeEntity::toDto)
    }

    override fun findOfficesWorkingWithType(type: CurrencyType, pageable: Pageable): Page<OfficeDto> {
        return officeRepository.findOfficesWorkingWithType(type, pageable).map { it.toDto(doINeedBank = false) }
    }


    override fun deleteOfficeById(id: Long) {
        officeRepository.deleteById(id)
    }
}