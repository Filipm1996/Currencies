package com.example.currencies.ui


import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.*
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonArrayRequest
import com.example.currencies.data.db.Currency
import com.example.currencies.data.repositories.DeafultCurrencyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject


class currencyViewModel(
    private val repository: DeafultCurrencyRepository
) : ViewModel() {

    var mediator : MediatorLiveData<List<Currency>> = MediatorLiveData()

    fun insertMyCurrency(currency:Currency) = CoroutineScope(Dispatchers.Main).launch {
        repository.insertMyCurrency(currency)
}
    fun getMyCurrencies() = repository.getMyAllCurrencies()

    fun deleteMyCurrencyByName(name:String) = CoroutineScope(Dispatchers.IO).launch { repository.deleteMyCurrencyByName(name)}


    fun getAllCurrencies () = repository.getAllCurrencies()


    fun deleteALlCurrencies() = repository.deleteAllCurrencies()

    suspend fun insertCurrencyToAllDatabase(currency: Currency) = CoroutineScope(Dispatchers.Main).launch {
        repository.insertCurrencyToAllDatabase(currency)
    }
    init {
        val mylist = repository.getMyAllCurrencies()
        val list = repository.getAllCurrencies()
        mediator.addSource(mylist) {
            list.value?.let { it1 -> updateRate(it, it1) }
        }
        mediator.addSource(list) {
            mylist.value?.let { it1 -> updateRate(it1, it) }
        }


    }
    private fun updateRate(myList : List<Currency>,allList : List<Currency>){
        for(item in myList){
            for(item1 in allList){
                if (item1.name == item.name){
                    val index = allList.indexOf(item1)
                    item.rate = allList[index].rate
                }
            }

        }
        mediator.value = myList
    }
    fun getUpdatedRates(): MediatorLiveData<List<Currency>> {
        return mediator
    }


    suspend fun getRecordsFromNomics() =  repository.getRecordsFromNomics()

    suspend fun getRecordsFromNomicsAndSaveToDb() {
            val list = getRecordsFromNomics()
            list.forEach {
                val price = String.format("%.4f",it.price.toDouble())
                price.replace(",",".")
                val currency = Currency(it.name, price, "crypto")
                insertCurrencyToAllDatabase(currency)
            }
    }

    fun getAPIRecords(url :String, cache : DiskBasedCache){
        CoroutineScope(Dispatchers.IO).launch {
        repository.gettingJsonStringFromNBP(url,cache)
        getRecordsFromNomicsAndSaveToDb()
        }
    }

}