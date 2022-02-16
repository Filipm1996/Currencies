package com.example.currencies.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currencyTable")
data class Currency (
    val name : String,
    var rate : String,
    @PrimaryKey(autoGenerate = true)
    val id : Int? = null
)

