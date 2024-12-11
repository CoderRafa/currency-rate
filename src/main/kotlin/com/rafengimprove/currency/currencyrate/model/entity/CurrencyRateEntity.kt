package com.rafengimprove.currency.currencyrate.model.entity

import com.rafengimprove.currency.currencyrate.model.dto.CurrencyRateDto
import com.rafengimprove.currency.currencyrate.model.enumerated.CurrencyType
import jakarta.persistence.*

@Entity
@Table(name = "currencyRate" )
class CurrencyRateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "currency_rate_seq")
    @SequenceGenerator(name ="currency_rate_seq")
    @Column(name = "id", nullable = false)
    open var id: Long? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    open lateinit var type: CurrencyType

    @Column(name = "rate", nullable = false)
    open var rate: Double = 0.0
}

fun CurrencyRateEntity.toDto(): CurrencyRateDto = CurrencyRateDto(id, type, rate)
