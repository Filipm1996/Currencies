package com.example.currencies.data.Retrofit.NBP

data class Rate(
    val code: String,
    val currency: String,
    val mid: Double
)