package com.example.currencies


import com.example.currencies.RoomDataBase.Currency
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
import com.example.currencies.RoomDataBase.CurrencyDao
import com.example.currencies.RoomDataBase.CurrencyDataBase
import com.example.currencies.databinding.ActivityMainBinding
import com.example.currencies2.ListOfCurrenciesFragment
import com.example.currencies2.MyCurrenciesFragment
import com.google.android.material.navigation.NavigationBarView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class PriceFormatter(
    val price : String
) {
    fun format(): String {
        return String.format("%.4f",price.toDouble())
    }
}
   fun testPriceFormatter (){
        val notFormattedValue : String = "4"
        val result : String = PriceFormatter(notFormattedValue).format()
        //sprawdzam czy result będzie równe 4.0000
    }
class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {


    lateinit var dataBase: CurrencyDataBase
    lateinit var dao : CurrencyDao
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
                    var price = object1.getString("mid")    // sprawdzic czy pobierze double
                    val price1 = String.format("%.4f",price.toDouble())
                    list.add(Currency(name,price1))
                }
                GlobalScope.launch(Dispatchers.IO) {
                    dataBase = Room.databaseBuilder(applicationContext, CurrencyDataBase::class.java, "currency_database").fallbackToDestructiveMigration().build()
                    dao = dataBase.currencyDao()
                    dao.deleteAll()
                    for (i in list) {
                        var currency = Currency(i.name, i.rate)
                        dao.insertCurrency(currency)
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
