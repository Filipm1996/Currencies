package com.example.currencies.ui


import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.currencies.data.repositories.DeafultCurrencyRepository
import com.example.currencies.data.repositories.repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



class currencyViewModel(
    private val repository: DeafultCurrencyRepository
) : ViewModel() {

    var mediator : MediatorLiveData<List<com.example.currencies.data.db.Currency>> = MediatorLiveData()

    suspend fun insertMyCurrency(currency: com.example.currencies.data.db.Currency) = CoroutineScope(Dispatchers.Main).launch {
        repository.insertMyCurrency(currency)
}
    fun getMyCurrencies() = repository.getMyAllCurrencies()

    fun deleteMyCurrencyByName(name:String) = repository.deleteMyCurrencyByName(name)

    fun deleteMyCurrencies () = repository.deleteMyCurrencies()

    fun getAllCurrencies () = repository.getAllCurrencies()

    fun deleteCurrencyByNameFromAllDatabase(name: String) = repository.deleteCurrencyByNameFromAllDatabase(name)

    fun deleteALlCurrencies() = repository.deleteAllCurrencies()

    suspend fun insertCurrencyToAllDatabase(currency: com.example.currencies.data.db.Currency) = CoroutineScope(Dispatchers.Main).launch {
        repository.insertCurrencyToAllDatabase(currency)
    }
    init {
        val mylist = repository.getMyAllCurrencies()
        val list = repository.getAllCurrencies()
        mediator.addSource(mylist, androidx.lifecycle.Observer {
            list.value?.let { it1 -> updateRate(it, it1) }
        })
        mediator.addSource(list, androidx.lifecycle.Observer {
            mylist.value?.let { it1 -> updateRate(it1, it) }
        })


    }
    private fun updateRate(myList : List<com.example.currencies.data.db.Currency>,allList : List<com.example.currencies.data.db.Currency>){

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
    fun getUpdatedRates(): MediatorLiveData<List<com.example.currencies.data.db.Currency>> {
        return mediator
    }
}