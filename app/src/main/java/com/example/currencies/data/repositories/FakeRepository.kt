package com.example.currencies.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.currencies.data.db.Currency


class FakeRepository : DeafultCurrencyRepository {

    val myCurrenciesItems = mutableListOf<Currency>()
    val myCurrenciesObservable = MutableLiveData<List<Currency>>(myCurrenciesItems)
    val allCurrenciesItems = mutableListOf<Currency>()
    val allCurrenciesObservable = MutableLiveData<List<Currency>>(allCurrenciesItems)

    override suspend fun insertMyCurrency(currency: Currency) {
        myCurrenciesItems.add(currency)
        refreshMyCurrenciesObservable()
    }

    override fun getMyAllCurrencies(): LiveData<List<Currency>> {
        return myCurrenciesObservable
    }

    override fun deleteMyCurrencyByName(name: String) {
        val deletedCurrency = myCurrenciesItems.find { it.name == name }
        myCurrenciesItems.remove(deletedCurrency)
        refreshMyCurrenciesObservable()
    }

    override fun deleteMyCurrencies() {
        myCurrenciesItems.clear()
        myCurrenciesObservable.postValue(myCurrenciesItems)
        refreshMyCurrenciesObservable()
    }

    override fun getAllCurrencies(): LiveData<List<Currency>> {
        return allCurrenciesObservable
    }

    override fun deleteCurrencyByNameFromAllDatabase(name: String) {
        val deletedCurrency = allCurrenciesItems.find { it.name == name }
        allCurrenciesItems.remove(deletedCurrency)
        refreshAllCurrenciesObservable()
    }

    override fun deleteAllCurrencies() {
        allCurrenciesItems.clear()
        allCurrenciesObservable.postValue(allCurrenciesItems)
        refreshAllCurrenciesObservable()
    }

    override suspend fun insertCurrencyToAllDatabase(currency: Currency) {
        allCurrenciesItems.add(currency)
        allCurrenciesObservable.postValue(allCurrenciesItems)
        refreshAllCurrenciesObservable()
    }

    private fun refreshMyCurrenciesObservable(){
        myCurrenciesObservable.postValue(myCurrenciesItems)
    }

    private fun refreshAllCurrenciesObservable(){
        allCurrenciesObservable.postValue(allCurrenciesItems)
    }
}