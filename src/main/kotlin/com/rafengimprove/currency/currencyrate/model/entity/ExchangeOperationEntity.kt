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

    @Column(name = "pre_exchange_amount")
    open var giveAmount: Double = 0.0

    @Enumerated(EnumType.STRING)
    @Column(name = "pre_exchange_currency_type")
    open lateinit var fromCurrencyType: CurrencyType

    @Column(name = "post_exchange_amount")
    open var receiveAmount: Double = 0.0

    @Enumerated(EnumType.STRING)
    @Column(name = "post_exchange_currency_type")
    open lateinit var toCurrencyType: CurrencyType

    @Column(name = "date_and_time_of_exchange")
    open lateinit var dateAndTimeOfExchange: LocalDateTime

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
        dateAndTimeOfExchange: LocalDateTime
    ): this() {
        this.operationType = operationType
        this.giveAmount = preExchangeAmount
        this.fromCurrencyType = preExchangeCurrencyType
        this.toCurrencyType = postExchangeCurrencyType
        this.dateAndTimeOfExchange = dateAndTimeOfExchange
    }

    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        val oEffectiveClass =
            if (other is HibernateProxy) other.hibernateLazyInitializer.persistentClass else other.javaClass
        val thisEffectiveClass =
            if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass else this.javaClass
        if (thisEffectiveClass != oEffectiveClass) return false
        other as ExchangeOperationEntity

        return id != null && id == other.id
    }

    final override fun hashCode(): Int =
        if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass.hashCode() else javaClass.hashCode()
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
    val clientDto = client ?: this.clientEntity?.toDto()

    exchangeOperationDto.officeDto = officeDto
    exchangeOperationDto.clientDto = clientDto

    return exchangeOperationDto
}