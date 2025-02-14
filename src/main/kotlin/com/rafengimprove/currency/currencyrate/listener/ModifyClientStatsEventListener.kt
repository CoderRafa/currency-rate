package com.rafengimprove.currency.currencyrate.listener

import com.rafengimprove.currency.currencyrate.model.event.ModifyClientStatsEvent
import com.rafengimprove.currency.currencyrate.service.impl.ClientStatsServiceImpl
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component

@Component
open class ModifyClientStatsEventListener(val clientStatsServiceImpl: ClientStatsServiceImpl): ApplicationListener<ModifyClientStatsEvent> {

    private val log = LoggerFactory.getLogger(ModifyClientStatsEventListener::class.java)
    override fun onApplicationEvent(event: ModifyClientStatsEvent) {
        log.debug("[ModifyClientStatsEvent]: Handle an event modify client stats with client id {}", event.exchangeDataDto.clientId )
        println("########################## It came here ########################################")
        clientStatsServiceImpl.modifyClientStats(event.exchangeDataDto)
    }
}