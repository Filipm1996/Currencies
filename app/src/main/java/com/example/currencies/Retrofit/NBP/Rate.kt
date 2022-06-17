package com.example.currencies.Retrofit.NBP

data class Rate(
    val code: String,
    val currency: String,
    val mid: Double
)