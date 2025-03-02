package com.rafengimprove.currency.currencyrate.model.dto

import com.rafengimprove.currency.currencyrate.model.type.ClientSortFieldType
import com.rafengimprove.currency.currencyrate.model.type.SortType

data class ClientSort(val field: ClientSortFieldType, val sortType: SortType)