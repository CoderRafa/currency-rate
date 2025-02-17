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

    @OneToMany(mappedBy = "officeEntity", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
    open var currencyRateEntities: MutableSet<CurrencyRateEntity> = mutableSetOf()

    @ManyToOne(cascade = [CascadeType.REFRESH], fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_entity_id")
    open var bankEntity: BankEntity? = null

    @OneToMany(mappedBy = "officeEntity", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
    open var exchangeOperationEntities: MutableSet<ExchangeOperationEntity> = mutableSetOf()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OfficeEntity) return false

        if (id != other.id) return false
        if (address != other.address) return false
        if (description != other.description) return false
        if (area != other.area) return false
//        if (currencyRateEntities != other.currencyRateEntities) return false
//        if (bankEntity != other.bankEntity) return false
        return exchangeOperationEntities == other.exchangeOperationEntities
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + address.hashCode()
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + area.hashCode()
        result = 31 * result + currencyRateEntities.hashCode()
        result = 31 * result + (bankEntity?.hashCode() ?: 0)
//        result = 31 * result + exchangeOperationEntities.hashCode()
        return result
    }
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

