package com.rafengimprove.currency.currencyrate.model.entity

import com.rafengimprove.currency.currencyrate.model.dto.CurrencyRateDto
import com.rafengimprove.currency.currencyrate.model.dto.OfficeDto
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType
import jakarta.persistence.*
import org.hibernate.proxy.HibernateProxy

@Entity
@Table(name = "currencyRate" )
open class CurrencyRateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "currency_rate_seq")
    @SequenceGenerator(name ="currency_rate_seq")
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



    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        val oEffectiveClass =
            if (other is HibernateProxy) other.hibernateLazyInitializer.persistentClass else other.javaClass
        val thisEffectiveClass =
            if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass else this.javaClass
        if (thisEffectiveClass != oEffectiveClass) return false
        other as CurrencyRateEntity

        return id != null && id == other.id
    }

    final override fun hashCode(): Int =
        if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass.hashCode() else javaClass.hashCode()
}

fun CurrencyRateEntity.toDto(
    office: OfficeDto? = null,
    doINeedCurrencies: Boolean = false,
    doINeedOffice: Boolean = false): CurrencyRateDto {
    val currencyRateDto = CurrencyRateDto(this.id, this.fromCurrencyType, this.toCurrencyType, this.buyRate, this.sellRate)
    if (doINeedOffice) {
        currencyRateDto.officeDto = office ?: this.officeEntity?.toDto(doINeedCurrencies = doINeedCurrencies, doINeedBank = true)
    }
    return currencyRateDto
}
