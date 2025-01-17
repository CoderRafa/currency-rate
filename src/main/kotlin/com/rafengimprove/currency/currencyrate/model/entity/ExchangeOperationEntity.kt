package com.rafengimprove.currency.currencyrate.model.entity

import com.rafengimprove.currency.currencyrate.model.dto.ClientDto
import com.rafengimprove.currency.currencyrate.model.dto.ExchangeOperationDto
import com.rafengimprove.currency.currencyrate.model.dto.OfficeDto
import com.rafengimprove.currency.currencyrate.model.type.CurrencyDirectionType
import com.rafengimprove.currency.currencyrate.model.type.CurrencyType
import jakarta.persistence.*
import org.hibernate.proxy.HibernateProxy
import java.time.LocalDateTime

@Entity
@Table(name = "exchange_operation")
open class ExchangeOperationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exchange_operation_seq")
    @SequenceGenerator(name = "exchange_operation_seq")
    @Column(name = "id", nullable = false)
    open var id: Long? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "currency_direction_type")
    lateinit var currencyDirectionType: CurrencyDirectionType

    @Column(name = "pre_exchange_amount")
    open var preExchangeAmount: Double = 0.0

    @Enumerated(EnumType.STRING)
    @Column(name = "pre_exchange_currency_type")
    lateinit var preExchangeCurrencyType: CurrencyType

    @Column(name = "post_exchange_amount")
    open var postExchangeAmount: Double = 0.0

    @Enumerated(EnumType.STRING)
    @Column(name = "post_exchange_currency_type")
    lateinit var postExchangeCurrencyType: CurrencyType

    @Column(name = "date_and_time_of_exchange")
    lateinit var dateAndTimeOfExchange: LocalDateTime

    @OneToOne(cascade = [CascadeType.REFRESH], orphanRemoval = true)
    @JoinColumn(name = "office_entity_id")
    lateinit var officeEntity: OfficeEntity

    @ManyToOne(cascade = [CascadeType.REFRESH])
    @JoinColumn(name = "client_entity_id")
    open var clientEntity: ClientEntity? = null

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
    client: ClientDto? = null
) {
    val exchangeOperationDto = ExchangeOperationDto(
        this.id, this.currencyDirectionType,
        this.preExchangeAmount, this.preExchangeCurrencyType,
        this.postExchangeAmount, this.postExchangeCurrencyType,
        this.dateAndTimeOfExchange
    )

    val officeDto = office ?: this.officeEntity.toDto()
    val clientDto = client ?: this.clientEntity?.toDto()

    exchangeOperationDto.officeDto = officeDto
    exchangeOperationDto.clientDto = clientDto
}