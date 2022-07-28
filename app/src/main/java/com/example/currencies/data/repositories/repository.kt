package com.example.currencies.data.repositories

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.currencies.data.Retrofit.NBP.RetrofitInstanceForNBP
import com.example.currencies.data.Retrofit.Nomics.NomicsResponseItem
import com.example.currencies.data.Retrofit.Nomics.RetrofitInstanceForNomics
import com.example.currencies.data.db.Currency
import com.example.currencies.data.db.CurrencyDataBase
import com.example.currencies.other.Constants
import java.lang.Exception

class repository(
    mContext : Context

): DeafultCurrencyRepository {


    private var listdb : CurrencyDataBase = Room.databaseBuilder(mContext, CurrencyDataBase::class.java, Constants.LIST_OF_CURRENCIES).fallbackToDestructiveMigration().build()

    private var mydb : CurrencyDataBase = Room.databaseBuilder(mContext, CurrencyDataBase::class.java, Constants.MY_LIST_DATABASE).fallbackToDestructiveMigration().build()

    override suspend fun insertMyCurrency(currency: Currency) = mydb.currencyDao().insertCurrency(currency)

    override fun getMyAllCurrencies() = mydb.currencyDao().getAllCurrencies()

    override fun deleteMyCurrencyByName(name: String) = mydb.currencyDao().deleteCurrencyByName(name)

    override fun deleteMyCurrencies() = mydb.currencyDao().deleteAll()

    override fun getAllCurrencies() = listdb.currencyDao().getAllCurrencies()

    override fun deleteCurrencyByNameFromAllDatabase(name: String) = listdb.currencyDao().deleteCurrencyByName(name)

    override fun deleteAllCurrencies() = listdb.currencyDao().deleteAll()

    override suspend fun insertCurrencyToAllDatabase(currency: Currency) = listdb.currencyDao().insertCurrency(currency)

    override fun updateRate(name:String,rate:String) = listdb.currencyDao().updateRate(name,rate)
    override suspend fun getRecordsFromNomics(): ArrayList<NomicsResponseItem> {
        var list = ArrayList<NomicsResponseItem>()
        try {
            list = RetrofitInstanceForNomics.api.getRecordsFromNomics()
            list.forEach {
                val price = String.format("%.4f",it.price.toDouble())
                price.replace(",",".")
                val currency = Currency(it.name, price, "crypto")
                insertCurrencyToAllDatabase(currency)
            }
        }catch (e : Exception){
            Log.e("Exception", e.message!!)
        }
        return list
        }

    override suspend fun getRecordsFromNBP(){
        try {
            val response = RetrofitInstanceForNBP.api.getNBPrecords()
            val listOfCurrencies = response[0].rates
                for (record in listOfCurrencies) {
                    val price = String.format("%.4f",record.mid)
                    val currency = Currency(record.currency, price, "normal")
                    insertCurrencyToAllDatabase(currency)
                }

        }catch (e : Exception){
            Log.e("Exception", e.message!!)
        }
    }

}