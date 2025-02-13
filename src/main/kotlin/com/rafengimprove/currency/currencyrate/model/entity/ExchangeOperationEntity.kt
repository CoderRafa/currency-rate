package com.rafengimprove.currency.currencyrate.model.entity

import com.rafengimprove.currency.currencyrate.model.dto.ClientDto
import com.rafengimprove.currency.currencyrate.model.dto.ExchangeOperationDto
import com.rafengimprove.currency.currencyrate.model.dto.OfficeDto
import com.rafengimprove.currency.currencyrate.model.type.OperationType
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType
import jakarta.persistence.*
import org.hibernate.proxy.HibernateProxy
import java.time.LocalDateTime

@Entity
@Table(name = "exchange_operation")
open class ExchangeOperationEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exchange_operation_seq")
    @SequenceGenerator(name = "exchange_operation_seq")
    @Column(name = "id", nullable = false)
    open var id: Long? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_type")
    open lateinit var operationType: OperationType

    @Column(name = "give_amount")
    open var giveAmount: Double = 0.0

    @Enumerated(EnumType.STRING)
    @Column(name = "from_currency_type")
    open lateinit var fromCurrencyType: CurrencyType

    @Column(name = "receive_amount")
    open var receiveAmount: Double = 0.0

    @Enumerated(EnumType.STRING)
    @Column(name = "to_currency_type")
    open lateinit var toCurrencyType: CurrencyType

    @Column(name = "date_and_time_of_exchange")
    open var dateAndTimeOfExchange: LocalDateTime? = null

    @ManyToOne(cascade = [CascadeType.REFRESH], fetch = FetchType.LAZY)
    @JoinColumn(name = "client_entity_id")
    open var clientEntity: ClientEntity? = null

    @ManyToOne(cascade = [CascadeType.REFRESH], fetch = FetchType.LAZY)
    @JoinColumn(name = "office_entity_id")
    open lateinit var officeEntity: OfficeEntity

    constructor(
        operationType: OperationType,
        preExchangeAmount: Double,
        preExchangeCurrencyType: CurrencyType,
        postExchangeCurrencyType: CurrencyType,
        dateAndTimeOfExchange: LocalDateTime?
    ) : this() {
        this.operationType = operationType
        this.giveAmount = preExchangeAmount
        this.fromCurrencyType = preExchangeCurrencyType
        this.toCurrencyType = postExchangeCurrencyType
        this.dateAndTimeOfExchange = dateAndTimeOfExchange
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ExchangeOperationEntity) return false

        if (id != other.id) return false
        if (operationType != other.operationType) return false
        if (giveAmount != other.giveAmount) return false
        if (fromCurrencyType != other.fromCurrencyType) return false
        if (receiveAmount != other.receiveAmount) return false
        if (toCurrencyType != other.toCurrencyType) return false
        if (dateAndTimeOfExchange != other.dateAndTimeOfExchange) return false
        if (clientEntity != other.clientEntity) return false
        return officeEntity == other.officeEntity
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + operationType.hashCode()
        result = 31 * result + giveAmount.hashCode()
        result = 31 * result + fromCurrencyType.hashCode()
        result = 31 * result + receiveAmount.hashCode()
        result = 31 * result + toCurrencyType.hashCode()
        result = 31 * result + dateAndTimeOfExchange.hashCode()
//        result = 31 * result + (clientEntity?.hashCode() ?: 0)
//        result = 31 * result + officeEntity.hashCode()
        return result
    }
}

fun ExchangeOperationEntity.toDto(
    office: OfficeDto? = null,
    client: ClientDto? = null,
    doINeedClient: Boolean = false
): ExchangeOperationDto {
    val exchangeOperationDto = ExchangeOperationDto(
        this.id, this.operationType,
        this.giveAmount, this.fromCurrencyType,
        this.receiveAmount, this.toCurrencyType,
        this.dateAndTimeOfExchange
    )

    val officeDto = office ?: this.officeEntity.toDto()
    if (doINeedClient) {
        val clientDto = client ?: this.clientEntity?.toDto()
        exchangeOperationDto.clientDto = clientDto
    }

    exchangeOperationDto.officeDto = officeDto

    return exchangeOperationDto
}