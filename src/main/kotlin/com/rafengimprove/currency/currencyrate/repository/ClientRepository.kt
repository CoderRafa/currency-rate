package com.rafengimprove.currency.currencyrate.repository

import com.rafengimprove.currency.currencyrate.model.entity.ClientEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ClientRepository: JpaRepository<ClientEntity, Long>