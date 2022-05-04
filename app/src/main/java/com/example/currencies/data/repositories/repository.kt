package com.example.currencies.data.repositories

import android.content.Context
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonArrayRequest
import com.example.currencies.Nomics.NomicsResponseItem
import com.example.currencies.Nomics.RetrofitInstanceForNomics
import com.example.currencies.data.db.Currency
import com.example.currencies.data.db.CurrencyDataBase
import com.example.currencies.other.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

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


    override suspend fun getRecordsFromNomics(): ArrayList<NomicsResponseItem> = RetrofitInstanceForNomics.api.getRecordsFromNomics()

    override fun gettingJsonStringFromNBP(url: String, cache: DiskBasedCache) {
        val network = BasicNetwork(HurlStack())
        val requestQueue = RequestQueue(cache, network).apply {
            start()
        }
        val jsonObjectRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                val ja2 : JSONObject? = response.getJSONObject(0)
                val ja1 : JSONArray = ja2!!.getJSONArray("rates")
                for(i in 0 until ja1.length()) {
                    val object1 = ja1.getJSONObject(i)
                    val name = object1.getString("currency")
                    val price = object1.getString("mid")
                    val price1 = String.format("%.4f",price.toDouble())
                    val currency = (Currency(name,price1,"normal"))
                    CoroutineScope(Dispatchers.IO).launch {
                        deleteAllCurrencies()
                        insertCurrencyToAllDatabase(currency)
                    }
                }
            },
            {
                println("error")
            })
        requestQueue.add(jsonObjectRequest)
    }

}