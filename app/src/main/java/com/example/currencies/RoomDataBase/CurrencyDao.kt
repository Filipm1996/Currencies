package com.example.currencies.RoomDataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CurrencyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrency(currency: Currency)

    @Query("SELECT * FROM currencyTable")
    suspend fun getAllCurrencies(): List<Currency>

    @Query("DELETE FROM currencyTable WHERE name= :name")
    suspend fun deleteCurrencyByName (name : String)

    @Query("DELETE FROM currencyTable")
    suspend fun deleteAll()

}