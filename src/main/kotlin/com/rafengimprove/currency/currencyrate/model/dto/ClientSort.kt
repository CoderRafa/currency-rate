package com.rafengimprove.currency.currencyrate.model.dto

import com.rafengimprove.currency.currencyrate.model.type.ClientSortFieldType
import com.rafengimprove.currency.currencyrate.model.type.SortType
import com.rafengimprove.currency.currencyrate.model.type.defineSortBy
import org.springframework.data.domain.Sort

data class ClientSort(val field: ClientSortFieldType, val sortType: SortType)

fun ClientSort.toSortRule() = when(field) {
    ClientSortFieldType.CREATED_DATE    -> sortType.toSort("dateAndTimeCreated")
    ClientSortFieldType.LAST_NAME       -> defineSortBy(sortType, "lastName")
}