package com.rafengimprove.currency.currencyrate.model.entity

import com.rafengimprove.currency.currencyrate.model.dto.BankDto
import com.rafengimprove.currency.currencyrate.model.dto.CurrencyRateDto
import com.rafengimprove.currency.currencyrate.model.dto.OfficeDto
import jakarta.persistence.*
import org.hibernate.proxy.HibernateProxy

@Entity
@Table(name = "office")
open class OfficeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "office_seq")
    @SequenceGenerator(name = "office_seq")
    @Column(name = "id", nullable = false)
    open var id: Long? = null

    @Column(name = "address")
    open lateinit var address: String

    @Column(name = "description")
    open var description: String? = null

    @Column(name = "area")
    open var area: Double = 0.0

    @OneToMany(mappedBy = "officeEntity", cascade = [CascadeType.ALL], orphanRemoval = true)
    open var currencyRateEntities: MutableSet<CurrencyRateEntity> = mutableSetOf()

    @ManyToOne(cascade = [CascadeType.REFRESH])
    @JoinColumn(name = "bank_entity_id")
    open var bankEntity: BankEntity? = null

    @OneToOne(mappedBy = "officeEntity", cascade = [CascadeType.ALL], orphanRemoval = true)
    open var exchangeOperationEntity: ExchangeOperationEntity? = null

    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        val oEffectiveClass =
            if (other is HibernateProxy) other.hibernateLazyInitializer.persistentClass else other.javaClass
        val thisEffectiveClass =
            if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass else this.javaClass
        if (thisEffectiveClass != oEffectiveClass) return false
        other as OfficeEntity

        return id != null && id == other.id
    }

    final override fun hashCode(): Int =
        if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass.hashCode() else javaClass.hashCode()
}

fun OfficeEntity.toDto(
    currencies: MutableSet<CurrencyRateDto>? = null,
    bank: BankDto? = null,
    doINeedOffices: Boolean = false,
    doINeedCurrencies: Boolean = true,
    doINeedBank: Boolean = true
): OfficeDto {
    val officeDto = OfficeDto(this.id, this.address, this.description, this.area)
    if (doINeedCurrencies) {
        val currencyDtos = currencies ?: this.currencyRateEntities.map { it.toDto() }
            .takeIf { this.currencyRateEntities.isNotEmpty() } ?: emptySet()
        officeDto.currencyRates.addAll(currencyDtos)
    }
    if (doINeedBank) {
        officeDto.bank = bank ?: this.bankEntity?.toDto(doINeedOffices = doINeedOffices)
    }
    return officeDto
}

