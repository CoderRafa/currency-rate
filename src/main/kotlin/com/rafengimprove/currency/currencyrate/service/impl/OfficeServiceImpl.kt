package com.rafengimprove.currency.currencyrate.service.impl

import com.rafengimprove.currency.currencyrate.model.dto.OfficeDto
import com.rafengimprove.currency.currencyrate.model.dto.toEntity
import com.rafengimprove.currency.currencyrate.model.entity.toDto
import com.rafengimprove.currency.currencyrate.repository.OfficeRepository
import com.rafengimprove.currency.currencyrate.service.OfficeService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class OfficeServiceImpl(val officeRepository: OfficeRepository) : OfficeService {

    private val log = LoggerFactory.getLogger(OfficeServiceImpl::class.java)
    override fun save(office: OfficeDto): OfficeDto {
        return if (officeRepository.existsByAddressIgnoreCase(office.address)) {
            officeRepository.findByAddressIgnoreCase(office.address).toDto()
        } else {
            officeRepository.save(office.toEntity()).toDto()
        }
    }

    override fun editByAddress(address: String, office: OfficeDto): OfficeDto? {
        return if (officeRepository.existsByAddressIgnoreCase(address)) {
            val officeToUpdate = getByAddress(address)
            officeToUpdate?.address = office.address
            officeToUpdate?.description = office.description
            officeToUpdate?.area = office.area
            officeToUpdate?.bank = office.bank
            officeToUpdate?.currencyRates = office.currencyRates.map { it.toEntity().toDto() }.toMutableSet()
            officeRepository.save(officeToUpdate!!.toEntity()).toDto()
        } else {
            null
        }
    }

    override fun getByAddress(address: String): OfficeDto? {
        return if (officeRepository.existsByAddressIgnoreCase(address)) {
            officeRepository.findByAddressIgnoreCase(address).toDto()
        } else {
            null
        }
    }

    override fun getAll(): List<OfficeDto> {
        return officeRepository.findAll().map { it.toDto() }
    }


}