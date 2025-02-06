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
    @Column(name = "from_currency_type")
    open var fromCurrencyType: CurrencyType? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "to_currency_type")
    open var toCurrencyType: CurrencyType? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_type")
    open var operationType: OperationType? = null

    @Column(name = "total")
    open var total: Double = 0.0

    @ManyToOne(cascade = [CascadeType.REFRESH], fetch = FetchType.LAZY)
    @JoinColumn(name = "client_entity_id")
    open var clientEntity: ClientEntity? = null

    constructor(
        fromCurrencyType: CurrencyType?,
        toCurrencyType: CurrencyType?,
        operationType: OperationType?,
        total: Double
    ) : this() {
        this.fromCurrencyType = fromCurrencyType
        this.toCurrencyType = toCurrencyType
        this.operationType = operationType
        this.total = total
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ClientStatsEntity) return false

        if (id != other.id) return false
        if (fromCurrencyType != other.fromCurrencyType) return false
        if (toCurrencyType != other.toCurrencyType) return false
        if (operationType != other.operationType) return false
        if (total != other.total) return false
        return clientEntity == other.clientEntity
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (fromCurrencyType?.hashCode() ?: 0)
        result = 31 * result + (toCurrencyType?.hashCode() ?: 0)
        result = 31 * result + (operationType?.hashCode() ?: 0)
        result = 31 * result + (total.hashCode() ?: 0)
        result = 31 * result + (clientEntity?.hashCode() ?: 0)
        return result
    }
}

fun ClientStatsEntity.toDto(clientDto: ClientDto? = null, doINeedClient: Boolean = true): ClientStatsDto {
    val clientStatsDto = ClientStatsDto(
        this.id, this.fromCurrencyType, this.toCurrencyType, this.operationType, this.total
    )

    if (doINeedClient) {
        clientStatsDto.clientDto = clientDto ?: this.clientEntity?.toDto(doINeedExchangeOperations = false)
    }

    return clientStatsDto
}