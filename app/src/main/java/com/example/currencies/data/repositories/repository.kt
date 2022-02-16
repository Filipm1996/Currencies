package com.example.currencies.data.repositories

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.room.Room
import com.example.currencies.data.db.Currency
import com.example.currencies.data.db.CurrencyDataBase
import com.example.currencies.other.Constants
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.yield
import javax.inject.Inject
import javax.inject.Named


class repository(
    mContext : Context

): deafultCurrencyRepository {


    var listdb : CurrencyDataBase = Room.databaseBuilder(mContext, CurrencyDataBase::class.java, Constants.LIST_OF_CURRENCIES).build()

    var mydb : CurrencyDataBase = Room.databaseBuilder(mContext, CurrencyDataBase::class.java, Constants.MY_LIST_DATABASE).build()

    override suspend fun insertMyCurrency(currency: Currency) = mydb.currencyDao().insertCurrency(currency)

    override fun getMyAllCurrencies() = mydb.currencyDao().getAllCurrencies()

    override fun deleteMyCurrencyByName(name: String) = mydb.currencyDao().deleteCurrencyByName(name)

    override fun deleteMyCurrencies() = mydb.currencyDao().deleteAll()

    override fun getAllCurrencies() = listdb.currencyDao().getAllCurrencies()

    override fun deleteCurrencyByNameFromAllDatabase(name: String) = listdb.currencyDao().deleteCurrencyByName(name)

    override fun deleteAllCurrencies() = listdb.currencyDao().deleteAll()

    override suspend fun insertCurrencyToAllDatabase(currency: Currency) = listdb.currencyDao().insertCurrency(currency)

}