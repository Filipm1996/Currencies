package com.example.currencies.ui


import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonArrayRequest
import com.example.currencies.data.db.Currency
import com.example.currencies.data.repositories.DeafultCurrencyRepository
import com.example.currencies.data.repositories.repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject


class currencyViewModel(
    private val repository: DeafultCurrencyRepository
) : ViewModel() {

    var mediator : MediatorLiveData<List<com.example.currencies.data.db.Currency>> = MediatorLiveData()

    suspend fun insertMyCurrency(currency: com.example.currencies.data.db.Currency) = CoroutineScope(Dispatchers.Main).launch {
        repository.insertMyCurrency(currency)
}
    fun getMyCurrencies() = repository.getMyAllCurrencies()

    fun deleteMyCurrencyByName(name:String) = CoroutineScope(Dispatchers.IO).launch { repository.deleteMyCurrencyByName(name)}


    fun getAllCurrencies () = repository.getAllCurrencies()


    fun deleteALlCurrencies() = repository.deleteAllCurrencies()

    suspend fun insertCurrencyToAllDatabase(currency: com.example.currencies.data.db.Currency) = CoroutineScope(Dispatchers.Main).launch {
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
                    var index = allList.indexOf(item1)
                    item.rate = allList[index].rate
                }
            }

        }
        mediator.value = myList
    }
    fun getUpdatedRates(): MediatorLiveData<List<Currency>> {
        return mediator
    }

     fun gettingJsonStringFromNBP(url :String, cache : DiskBasedCache) {

        val network = BasicNetwork(HurlStack())
        val requestQueue = RequestQueue(cache, network).apply {
            start()
        }
        val jsonObjectRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                val list = mutableListOf<Currency>()
                val ja2 : JSONObject? = response.getJSONObject(0)
                val ja1 : JSONArray = ja2!!.getJSONArray("rates")
                for(i in 0 until ja1.length()) {
                    val object1 = ja1.getJSONObject(i)
                    val name = object1.getString("currency")
                    val price = object1.getString("mid")
                    val price1 = String.format("%.4f",price.toDouble())
                    list.add(Currency(name,price1))
                }
                CoroutineScope(Dispatchers.IO).launch {
                    deleteALlCurrencies()
                    for (i in list) {
                        val currency = Currency(i.name, i.rate)

                        insertCurrencyToAllDatabase(currency)
                    }
                }

            },
            {
                println("error")
            })
        requestQueue.add(jsonObjectRequest)

    }

    suspend fun getRecordsFromNomics() =  repository.getRecordsFromNomics()
}