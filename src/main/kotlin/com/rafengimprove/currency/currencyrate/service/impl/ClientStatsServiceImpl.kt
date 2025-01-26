package com.rafengimprove.currency.currencyrate.service.impl

import com.rafengimprove.currency.currencyrate.model.dto.ClientStatsDto
import com.rafengimprove.currency.currencyrate.model.dto.toEntity
import com.rafengimprove.currency.currencyrate.model.entity.toDto
import com.rafengimprove.currency.currencyrate.model.type.CurrencyDirectionType
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType
import com.rafengimprove.currency.currencyrate.repository.ClientStatsRepository
import com.rafengimprove.currency.currencyrate.service.ClientStatsService
import org.springframework.stereotype.Service

@Service
class ClientStatsServiceImpl(val clientStatsRepository: ClientStatsRepository): ClientStatsService {
    override fun save(clientStatsDto: ClientStatsDto): ClientStatsDto {
        return clientStatsRepository.save(clientStatsDto.toEntity()).toDto()
    }

    override fun getClientStatsByClientIdCurrencyTypeAndDirection(
        id: Long,
        currencyType: CurrencyType,
        currencyDirectionType: CurrencyDirectionType
    ): ClientStatsDto? {
        return clientStatsRepository.findByClientIdAndCurrencyTypeAndCurrencyDirectionType(
            id,
            currencyType,
            currencyDirectionType
        ).toDto()
    }

    override fun editByClientIdCurrencyTypeAndDirection(
        id: Long,
        currencyType: CurrencyType,
        currencyDirectionType: CurrencyDirectionType,
        amount: Double
    ): ClientStatsDto? {
        val clientStatToChange = getClientStatsByClientIdCurrencyTypeAndDirection(id, currencyType, currencyDirectionType)
        clientStatToChange.let { it?.total = it?.total?.plus(amount) }

        return clientStatToChange
    }
}