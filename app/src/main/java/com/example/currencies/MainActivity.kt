package com.example.currencies


import com.example.currencies.data.db.Currency
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.commit
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonArrayRequest
import com.example.currencies.data.db.CurrencyDao
import com.example.currencies.data.db.CurrencyDataBase
import com.example.currencies.data.repositories.repository
import com.example.currencies.databinding.ActivityMainBinding
import com.example.currencies2.ListOfCurrenciesFragment
import com.example.currencies2.MyCurrenciesFragment
import com.google.android.material.navigation.NavigationBarView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject



class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {


    lateinit var repository: repository
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        repository = repository(this)
        gettingJsonString("https://api.nbp.pl/api/exchangerates/tables/A/?format=json")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNav.setOnItemSelectedListener(this)
        myCurrenciesClicked()
    }





    private fun gettingJsonString(url :String) {

        val cache = DiskBasedCache(cacheDir, 1024* 1024)
        val network = BasicNetwork(HurlStack())
        val requestQueue = RequestQueue(cache, network).apply {
            start()

        }
        val jsonObjectRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                    var list = mutableListOf<Currency>()
                   val Ja2 : JSONObject? = response.getJSONObject(0)
                    val Ja1 : JSONArray = Ja2!!.getJSONArray("rates")
                for(i in 0 until Ja1.length()) {
                    val object1 = Ja1.getJSONObject(i)
                    val name = object1.getString("currency")
                    var price = object1.getString("mid")
                    val price1 = String.format("%.4f",price.toDouble())
                    list.add(Currency(name,price1))
                }
                CoroutineScope(Dispatchers.IO).launch {
                    repository.deleteAllCurrencies()
                    for (i in list) {
                        var currency = Currency(i.name, i.rate)

                        repository.insertCurrencyToAllDatabase(currency)
                    }
                    }

                },
            { error ->
                println("error")
    })
        requestQueue.add(jsonObjectRequest)

}

    override fun onNavigationItemSelected(item: MenuItem) =
        when (item.itemId){
            R.id.my_currencies-> {
                myCurrenciesClicked()
            }

            R.id.list_of_currencies -> {
                listOfCurrenciesClicked()
            }   else ->false
        }


    fun myCurrenciesClicked(): Boolean {
        supportFragmentManager.commit {
            replace(R.id.fragment_layout, MyCurrenciesFragment())
        }
        return true
    }
    private fun listOfCurrenciesClicked(): Boolean {
        supportFragmentManager.commit {
            replace(R.id.fragment_layout, ListOfCurrenciesFragment())
        }
        return true
    }

}
