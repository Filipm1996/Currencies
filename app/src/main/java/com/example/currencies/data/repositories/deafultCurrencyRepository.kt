package com.example.currencies.data.repositories

import androidx.lifecycle.LiveData
import com.example.currencies.data.Retrofit.Nomics.NomicsResponseItem
import com.example.currencies.data.db.Currency

interface DeafultCurrencyRepository {

    suspend fun insertMyCurrency(currency: Currency)

    fun getMyAllCurrencies(): LiveData<List<Currency>>

    fun deleteMyCurrencyByName (name : String)

    fun deleteMyCurrencies()

    fun getAllCurrencies() : LiveData<List<Currency>>

    fun deleteCurrencyByNameFromAllDatabase(name:String)

    fun deleteAllCurrencies()

    suspend fun insertCurrencyToAllDatabase(currency: Currency)

    suspend fun getRecordsFromNomics():  ArrayList<NomicsResponseItem>

    suspend fun getRecordsFromNBP()

    fun updateRate(name:String,rate:String)
}