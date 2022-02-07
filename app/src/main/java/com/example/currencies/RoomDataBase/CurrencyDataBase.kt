package com.example.currencies.RoomDataBase

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities =[Currency::class],
    version = 3
)

abstract class CurrencyDataBase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao

}
