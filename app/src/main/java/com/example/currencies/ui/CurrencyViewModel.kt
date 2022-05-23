package com.example.currencies.ui



import androidx.lifecycle.*
import com.android.volley.toolbox.DiskBasedCache
import com.example.currencies.data.db.Currency
import com.example.currencies.data.repositories.DeafultCurrencyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



class CurrencyViewModel(
    private val repository: DeafultCurrencyRepository
) : ViewModel() {

    var mediator : MediatorLiveData<List<Currency>> = MediatorLiveData()

    fun insertMyCurrency(currency:Currency) = CoroutineScope(Dispatchers.Main).launch {
        repository.insertMyCurrency(currency)
}
    fun getMyCurrencies() = repository.getMyAllCurrencies()

    fun deleteMyCurrencyByName(name:String) = CoroutineScope(Dispatchers.IO).launch { repository.deleteMyCurrencyByName(name)}


    fun getAllCurrencies () = repository.getAllCurrencies()

    fun deleteMyCurrencies() = repository.deleteMyCurrencies()


    suspend fun insertCurrencyToAllDatabase(currency: Currency) = CoroutineScope(Dispatchers.Main).launch {
        repository.insertCurrencyToAllDatabase(currency)
    }

    private fun updateRate(myList : List<Currency>,allList : List<Currency>){
        val usd = allList.find { it.name=="dolar amerykański" }
        for(item in myList){
            for(item1 in allList){
                if(item.typeOfCurrency!="crypto"){
                if (item1.name == item.name) {
                    val index = allList.indexOf(item1)
                    item.rate = allList[index].rate
                }
                }else if(item.typeOfCurrency == "crypto"){
                    if(item1.name == item.name){
                        item.rate = (item1.rate.toDouble() * usd!!.rate.toDouble()).toString()
                    }
                }
            }

        }
        mediator.value = myList
    }
    fun getUpdatedRates(): MediatorLiveData<List<Currency>> {
        val mylist = repository.getMyAllCurrencies()
        val list = repository.getAllCurrencies()
        mediator.addSource(mylist) {
            list.value?.let { it1 -> updateRate(it, it1) }
        }
        mediator.addSource(list) {
            mylist.value?.let { it1 -> updateRate(it1, it) }
        }
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