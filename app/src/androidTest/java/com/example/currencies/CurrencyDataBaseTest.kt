package com.example.currencies



import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.currencies.data.db.Currency
import com.example.currencies.data.db.CurrencyDao
import com.example.currencies.data.db.CurrencyDataBase
import com.example.currencies.other.Constants
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class CurrencyDataBaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()



    lateinit var database: CurrencyDataBase
    private lateinit var currencyDao: CurrencyDao

    @Before
    fun setUp(){
        database = Room.databaseBuilder(ApplicationProvider.getApplicationContext(), CurrencyDataBase::class.java, Constants.LIST_OF_CURRENCIES).allowMainThreadQueries().build()
        currencyDao = database.currencyDao()
    }

    @After
    fun teardown(){
        database.close()
    }

    @Test
    fun insertCurrencyTest () = runBlocking{
        val currencyToAdd = Currency("PLN", "0.241", "normal",1)
        currencyDao.insertCurrency(currencyToAdd)
        val allCurrencies = currencyDao.getAllCurrencies().getOrAwaitValue()
        assertThat(allCurrencies.contains(currencyToAdd)).isTrue()
    }

    @Test
    fun deleteCurrencyByNameTest() = runBlocking {
        val currencyToDelete = Currency("PLN", "0.241","normal")
        currencyDao.insertCurrency(currencyToDelete)
        currencyDao.deleteCurrencyByName(currencyToDelete.name)
        val allCurrencies = currencyDao.getAllCurrencies().getOrAwaitValue()
        assertThat(allCurrencies.contains(currencyToDelete)).isFalse()
    }

    @Test
    fun deleteAllTest () = runBlocking {
        val currencyToAdd1 = Currency("PLN", "0.241","normal")
        val currencyToAdd2 = Currency("PLNN", "0.242","normal")
        currencyDao.insertCurrency(currencyToAdd1)
        currencyDao.insertCurrency(currencyToAdd2)
        currencyDao.deleteAll()
        val allCurrencies = currencyDao.getAllCurrencies().getOrAwaitValue()
        assertThat(allCurrencies.isEmpty()).isTrue()

    }
}