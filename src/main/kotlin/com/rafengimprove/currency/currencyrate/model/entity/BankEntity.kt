package com.rafengimprove.currency.currencyrate.model.entity

import com.rafengimprove.currency.currencyrate.model.dto.BankDto
import com.rafengimprove.currency.currencyrate.model.dto.OfficeDto
import jakarta.persistence.*

@Entity
@Table(name = "bank")
open class BankEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bank_seq")
    @SequenceGenerator(name = "bank_seq")
    @Column(name = "id", nullable = false)
    open var id: Long? = null

    @Column(name = "name")
    open lateinit var name: String

    @Column(name = "description")
    open var description: String? = null

    @OneToMany(mappedBy = "bankEntity", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
    open var officeEntities: MutableSet<OfficeEntity> = mutableSetOf()
}

fun BankEntity.toDto(
    offices: MutableSet<OfficeDto>? = null,
    doINeedOffices: Boolean = true,
    doINeedCurrencies: Boolean = false
): BankDto {

    val bankDto = BankDto(
        this.id, this.name, this.description
    )

    if (doINeedOffices) {
        val officeDtos = offices ?: this.officeEntities.map {
            it.toDto(
                bank = bankDto,
                doINeedCurrencies = doINeedCurrencies,
                doINeedBank = false
            )
        }
            .takeIf { this.officeEntities.isNotEmpty() } ?: emptySet()
        bankDto.offices.addAll(officeDtos)
    }

    return bankDto
}