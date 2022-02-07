package com.example.currencies.CurrencyAPI

data class MyDataItem(
    val effectiveDate: String,
    val no: String,
    val rates: List<Rate>,
    val table: String

)

