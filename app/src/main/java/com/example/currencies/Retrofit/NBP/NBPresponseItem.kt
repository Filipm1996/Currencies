package com.example.currencies.Retrofit.NBP

data class NBPresponseItem(
    val effectiveDate: String,
    val no: String,
    val rates: List<Rate>,
    val table: String
)