package com.rafengimprove.currency.currencyrate.service.impl

import com.rafengimprove.currency.currencyrate.model.dto.OfficeDto
import com.rafengimprove.currency.currencyrate.model.dto.toEntity
import com.rafengimprove.currency.currencyrate.model.entity.toDto
import com.rafengimprove.currency.currencyrate.repository.BankRepository
import com.rafengimprove.currency.currencyrate.repository.OfficeRepository
import com.rafengimprove.currency.currencyrate.service.BankService
import com.rafengimprove.currency.currencyrate.service.OfficeService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class OfficeServiceImpl(val officeRepository: OfficeRepository, val bankRepository: BankRepository) : OfficeService {

    private val log = LoggerFactory.getLogger(OfficeServiceImpl::class.java)

    override fun save(bankId: Long, office: OfficeDto): OfficeDto {
        return if (officeRepository.existsByAddressIgnoreCaseAndBankEntity_Id(office.address, bankId)) {
            officeRepository.findByAddressIgnoreCaseAndBankEntity_Id(office.address, bankId).toDto(doINeedOffices = false)
        } else {
            officeRepository.save(office.toEntity(bankRepository.findById(bankId).get())).toDto(doINeedOffices = false, doINeedBank = true)
        }
    }

    override fun editById(bankId: Long, office: OfficeDto): OfficeDto? {
        return if (officeRepository.existsById(office.id!!)) {
            val officeToUpdate = getById(office.id)
            officeToUpdate?.address = office.address
            officeToUpdate?.description = office.description
            officeToUpdate?.area = office.area
            officeToUpdate?.bank = office.bank
            officeToUpdate?.currencyRates = office.currencyRates.map { it.toEntity().toDto() }.toMutableSet()
            officeRepository.save(officeToUpdate!!.toEntity(bankRepository.findById(bankId).get())).toDto(doINeedOffices = false, doINeedBank = true)
        } else {
            null
        }
    }

    override fun getById(officeId: Long): OfficeDto? {
        return if (officeRepository.existsById(officeId)) {
            officeRepository.findById(officeId).map { it.toDto(doINeedOffices = false) }.orElseThrow()
        } else {
            null
        }
    }

    override fun getAllByBank(bankId: Long): List<OfficeDto> {
        return officeRepository.findByBankEntity_Id(bankId).map { it.toDto(doINeedOffices = false) }
    }

}