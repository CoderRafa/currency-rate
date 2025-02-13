package com.rafengimprove.currency.currencyrate.model.entity

import com.rafengimprove.currency.currencyrate.model.dto.CurrencyRateDto
import com.rafengimprove.currency.currencyrate.model.dto.OfficeDto
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType
import jakarta.persistence.*
import org.hibernate.proxy.HibernateProxy

@Entity
@Table(name = "currencyRate")
open class CurrencyRateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "currency_rate_seq")
    @SequenceGenerator(name = "currency_rate_seq")
    @Column(name = "id", nullable = false)
    open var id: Long? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "from_currency_type", nullable = false)
    open lateinit var fromCurrencyType: CurrencyType

    @Enumerated(EnumType.STRING)
    @Column(name = "to_currency_type")
    open lateinit var toCurrencyType: CurrencyType

    @Column(name = "buy_rate")
    open var buyRate: Double = 0.0

    @Column(name = "sell_rate")
    open var sellRate: Double = 0.0

    @ManyToOne(cascade = [CascadeType.REFRESH], fetch = FetchType.LAZY)
    @JoinColumn(name = "office_entity_id")
    open var officeEntity: OfficeEntity? = null
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CurrencyRateEntity) return false

        if (id != other.id) return false
        if (fromCurrencyType != other.fromCurrencyType) return false
        if (toCurrencyType != other.toCurrencyType) return false
        if (buyRate != other.buyRate) return false
        if (sellRate != other.sellRate) return false
        return officeEntity == other.officeEntity
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + fromCurrencyType.hashCode()
        result = 31 * result + toCurrencyType.hashCode()
        result = 31 * result + buyRate.hashCode()
        result = 31 * result + sellRate.hashCode()
//        result = 31 * result + (officeEntity?.hashCode() ?: 0)
        return result
    }
}

fun CurrencyRateEntity.toDto(
    office: OfficeDto? = null,
    doINeedCurrencies: Boolean = false,
    doINeedOffice: Boolean = false
): CurrencyRateDto {
    val currencyRateDto =
        CurrencyRateDto(this.id, this.fromCurrencyType, this.toCurrencyType, this.buyRate, this.sellRate)
    if (doINeedOffice) {
        currencyRateDto.officeDto =
            office ?: this.officeEntity?.toDto(doINeedCurrencies = doINeedCurrencies, doINeedBank = true)
    }
    return currencyRateDto
}
