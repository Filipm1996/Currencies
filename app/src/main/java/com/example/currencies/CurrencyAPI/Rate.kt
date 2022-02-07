package com.example.currencies.CurrencyAPI

data class Rate(
    val code: String,
    val currency: String,
    val mid: Double
)