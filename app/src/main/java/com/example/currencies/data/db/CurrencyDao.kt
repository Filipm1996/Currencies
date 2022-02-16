package com.example.currencies.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CurrencyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrency(currency: Currency)

    @Query("SELECT * FROM currencyTable")
    fun getAllCurrencies(): LiveData<List<Currency>>

    @Query("DELETE FROM currencyTable WHERE name= :name")
    fun deleteCurrencyByName (name : String)

    @Query("DELETE FROM currencyTable")
    fun deleteAll()

}