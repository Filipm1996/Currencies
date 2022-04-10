package com.example.currencies.data.repositories

import android.content.Context
import androidx.room.Room
import com.example.currencies.CurrencyAPI.Retrofit.Nomics.NomicsResponseItem
import com.example.currencies.CurrencyAPI.Retrofit.Nomics.RetrofitInstanceForNomics
import com.example.currencies.data.db.Currency
import com.example.currencies.data.db.CurrencyDataBase
import com.example.currencies.other.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class repository(
    mContext : Context

): DeafultCurrencyRepository {


    private var listdb : CurrencyDataBase = Room.databaseBuilder(mContext, CurrencyDataBase::class.java, Constants.LIST_OF_CURRENCIES).build()

    private var mydb : CurrencyDataBase = Room.databaseBuilder(mContext, CurrencyDataBase::class.java, Constants.MY_LIST_DATABASE).build()

    override suspend fun insertMyCurrency(currency: Currency) = mydb.currencyDao().insertCurrency(currency)

    override fun getMyAllCurrencies() = mydb.currencyDao().getAllCurrencies()

    override fun deleteMyCurrencyByName(name: String) = mydb.currencyDao().deleteCurrencyByName(name)

    override fun deleteMyCurrencies() = mydb.currencyDao().deleteAll()

    override fun getAllCurrencies() = listdb.currencyDao().getAllCurrencies()

    override fun deleteCurrencyByNameFromAllDatabase(name: String) = listdb.currencyDao().deleteCurrencyByName(name)

    override fun deleteAllCurrencies() = listdb.currencyDao().deleteAll()

    override suspend fun insertCurrencyToAllDatabase(currency: Currency) = listdb.currencyDao().insertCurrency(currency)


    override suspend fun getRecordsFromNomics(): ArrayList<NomicsResponseItem> = RetrofitInstanceForNomics.api.getRecordsFromNomics()

}