package com.rafengimprove.currency.currencyrate.model.type

import org.springframework.data.domain.Sort

val DEFAULT_SORT = SortType.DESC.toSort("id")

enum class SortType {
    ASC {
        override fun toSort(fieldName: String): Sort {
            return Sort.by(fieldName).ascending()
        }
    },
    DESC {
        override fun toSort(fieldName: String): Sort {
            return Sort.by(fieldName).descending()
        }
    };

    abstract fun toSort(fieldName: String): Sort
}

fun defineSortBy(sortType: SortType, fieldName: String) = SortType.entries.find { it == sortType }!!.toSort(fieldName)