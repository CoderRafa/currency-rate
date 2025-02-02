package com.rafengimprove.currency.currencyrate.model.entity

import com.rafengimprove.currency.currencyrate.model.dto.ClientDto
import com.rafengimprove.currency.currencyrate.model.dto.ClientStatsDto
import com.rafengimprove.currency.currencyrate.model.type.OperationType
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType
import jakarta.persistence.*

@Entity
open class ClientStatsEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_stats_seq")
    @SequenceGenerator(name = "client_stats_seq")
    @Column(name = "id", nullable = false)
    open var id: Long? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "currency_type")
    open var currencyType: CurrencyType? = null


    @Enumerated(EnumType.STRING)
    @Column(name = "currency_direction_type")
    open var operationType: OperationType? = null

    @Column(name = "total")
    open var total: Double = 0.0

    @ManyToOne(cascade = [CascadeType.REFRESH], fetch = FetchType.LAZY)
    @JoinColumn(name = "client_entity_id")
    open var clientEntity: ClientEntity? = null

    constructor(
        currencyType: CurrencyType?,
        operationType: OperationType?,
        total: Double
    ): this() {
        this.currencyType = currencyType
        this.operationType = operationType
        this.total = total
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ClientStatsEntity) return false

        if (id != other.id) return false
        if (currencyType != other.currencyType) return false
        if (total != other.total) return false
        return clientEntity == other.clientEntity
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (currencyType?.hashCode() ?: 0)
        result = 31 * result + (total.hashCode() ?: 0)
        result = 31 * result + (clientEntity?.hashCode() ?: 0)
        return result
    }
}

fun ClientStatsEntity.toDto(clientDto: ClientDto? = null, doINeedClient: Boolean = true): ClientStatsDto {
    val clientStatsDto = ClientStatsDto(
        this.id, this.currencyType, this.operationType, this.total
    )

    if (doINeedClient) {
        clientStatsDto.clientDto = clientDto ?: this.clientEntity?.toDto(doINeedExchangeOperations = false)
    }

    return clientStatsDto
}