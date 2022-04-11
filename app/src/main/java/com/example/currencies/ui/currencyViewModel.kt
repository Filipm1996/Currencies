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

     fun gettingJsonStringFromNBP(url :String, cache : DiskBasedCache) {

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
                        deleteALlCurrencies()
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
        gettingJsonStringFromNBP(url,cache)
        getRecordsFromNomicsAndSaveToDb()
        }
    }

    fun setAlertDialog(currency: Currency , mContext : Context , lifecycleOwner: LifecycleOwner) {

        val buildier = AlertDialog.Builder(mContext)
        buildier.setMessage("Do you want to add ${currency.name}?")
        buildier.setCancelable(true)
        buildier.setPositiveButton("yes") { dialog, _ ->

            repository.getMyAllCurrencies().observeOnce(lifecycleOwner) {
                if (it != null) {
                    val myList = it
                    if (myList.contains(currency)) {

                        Toast.makeText(
                            mContext,
                            "${currency.name} is in favourites",
                            Toast.LENGTH_LONG
                        ).show()

                    } else {
                        insertMyCurrency(currency)
                        Toast.makeText(
                            mContext,
                            "Added ${currency.name}",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }
            }
        }
        buildier.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val alert = buildier.create()
        alert.show()

    }
    fun setAddButtonAlertDialog(cryptoCurrency: Currency, mContext:Context, lifecycleOwner: LifecycleOwner) {
        val buildier = AlertDialog.Builder(mContext)
        buildier.setMessage("Do you want to add ${cryptoCurrency.name}?")
        buildier.setCancelable(true)
        buildier.setPositiveButton("yes") { dialog, _ ->

            repository.getMyAllCurrencies().observe(lifecycleOwner, Observer {
                if (it != null) {
                    val myList = it
                    val formattedPrice = String.format("%.4f", cryptoCurrency.rate.toDouble())
                    val cryptoItem = Currency(cryptoCurrency.name, formattedPrice, "crypto")
                    var isIn = false
                    for (i in myList) {
                        if (i.name == cryptoCurrency.name) {
                            isIn = true
                        }
                    }
                    if (isIn) {
                        Toast.makeText(
                            mContext,
                            "${cryptoCurrency.name} is in favourites",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        CoroutineScope(Dispatchers.IO).launch {
                            repository.insertMyCurrency(cryptoItem)
                        }
                        Toast.makeText(
                            mContext,
                            "Added ${cryptoItem.name}",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }
            })
        }
        buildier.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val alert = buildier.create()
        alert.show()

    }

    fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }
}