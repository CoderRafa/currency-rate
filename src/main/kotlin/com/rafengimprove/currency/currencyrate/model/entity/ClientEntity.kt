package com.rafengimprove.currency.currencyrate.model.entity

import com.rafengimprove.currency.currencyrate.model.dto.ClientDto
import com.rafengimprove.currency.currencyrate.model.dto.ExchangeOperationDto
import jakarta.persistence.*

@Entity
@Table(name = "clent")
open class ClientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_seq")
    @SequenceGenerator(name = "client_seq")
    @Column(name = "id", nullable = false)
    open var id: Long? = null

    @Column(name = "firs_name")
    open var firsName: String? = null

    @Column(name = "last_name")
    open var lastName: String? = null

    @Column(name = "passport_number")
    open var passportNumber: String? = null

    @Column(name = "email")
    open var email: String? = null

    @OneToMany(mappedBy = "clientEntity", cascade = [CascadeType.ALL], orphanRemoval = true)
    open var exchangeOperationEntities: MutableSet<ExchangeOperationEntity> = mutableSetOf()
}

fun ClientEntity.toDto(
    exchangeOperations: MutableSet<ExchangeOperationDto>? = null
) {
   val clientDto = ClientDto(this.id, this.firsName, this.lastName, this.passportNumber, this.email)

   val exchangeOperationDtos = exchangeOperations ?: this.exchangeOperationEntities.map { it.toDto(client = clientDto) }.takeIf { this.exchangeOperationEntities.isNotEmpty() } ?: emptySet()

    clientDto.exchangeOperations.addAll(exchangeOperationDtos)
}
