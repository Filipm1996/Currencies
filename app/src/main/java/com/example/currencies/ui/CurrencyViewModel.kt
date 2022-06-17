package com.example.currencies.ui



import android.util.Log
import androidx.lifecycle.*
import com.android.volley.toolbox.DiskBasedCache
import com.example.currencies.data.db.Currency
import com.example.currencies.data.repositories.DeafultCurrencyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception


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
        val usdRate = usd?.rate ?: "4.40"
        for(item in myList){
            for(item1 in allList){
                if(item.typeOfCurrency!="crypto"){
                if (item1.name == item.name) {
                    val index = allList.indexOf(item1)
                    item.rate = allList[index].rate
                }
                }else if(item.typeOfCurrency == "crypto"){
                    if(item1.name == item.name){
                        item.rate = (item1.rate.toDouble() * usdRate.toDouble()).toString()
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

    suspend fun getRecordsFromNomicsAndSaveToDb() =repository.getRecordsFromNomics()

    suspend fun getRecordsFromNBPAndSaveToDb () = repository.getRecordsFromNBP()

    fun getAPIRecords(){
        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteAllCurrencies()
            getRecordsFromNBPAndSaveToDb()
            getRecordsFromNomicsAndSaveToDb()
        }
    }

}