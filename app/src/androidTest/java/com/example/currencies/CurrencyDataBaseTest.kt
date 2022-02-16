package com.example.currencies

import androidx.test.filters.SmallTest
import com.example.currencies.data.db.Currency
import com.example.currencies.data.db.CurrencyDao
import com.example.currencies.data.db.CurrencyDataBase
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named


@HiltAndroidTest
@SmallTest
class CurrencyDataBaseTest {

    @get:Rule
    var hiltRule= HiltAndroidRule(this)


    @Inject
    @Named("db_test")
    lateinit var database: CurrencyDataBase
    lateinit var currencyDao: CurrencyDao

    @Before
    fun setUp(){
        hiltRule.inject()
        currencyDao = database.currencyDao()
    }

    @After
    fun teardown(){
        database.close()
    }

    @Test
    fun insertCurrencyTest () = runBlocking{
        val currencyToAdd = Currency("PLN", "0.241", 1)
        currencyDao.insertCurrency(currencyToAdd)
        val allCurrencies = currencyDao.getAllCurrencies()
        assertThat(allCurrencies.value?.contains(currencyToAdd)).isTrue()
    }

    @Test
    fun deleteCurrencyByNameTest() = runBlocking {
        val currencyToDelete = Currency("PLN", "0.241")
        currencyDao.insertCurrency(currencyToDelete)
        currencyDao.deleteCurrencyByName(currencyToDelete.name)
        val allCurrencies = currencyDao.getAllCurrencies()
        assertThat(allCurrencies.value?.contains(currencyToDelete)).isFalse()
    }

    @Test
    fun deleteAllTest () = runBlocking {
        val currencyToAdd1 = Currency("PLN", "0.241")
        val currencyToAdd2 = Currency("PLNN", "0.242")
        currencyDao.insertCurrency(currencyToAdd1)
        currencyDao.insertCurrency(currencyToAdd2)
        currencyDao.deleteAll()
        val allCurrencies = currencyDao.getAllCurrencies()
        assertThat(allCurrencies.value?.isEmpty()).isTrue()

    }
}