package com.example.currencies.Activities_and_Fragments



import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.android.volley.toolbox.DiskBasedCache
import com.example.currencies.R
import com.example.currencies.data.repositories.repository
import com.example.currencies.databinding.ActivityMainBinding
import com.example.currencies.ui.CurrencyViewModel
import com.example.currencies.ui.CurrencyViewModelFactory
import com.google.android.material.navigation.NavigationBarView


class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {

    private lateinit var viewModel : CurrencyViewModel
    private lateinit var repository: repository
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNav.setOnItemSelectedListener(this)
        setUpViewModel()
        viewModel.getAPIRecords(
            "https://api.nbp.pl/api/exchangerates/tables/A/?format=json",
                DiskBasedCache(cacheDir, 1024 * 1024)
            )
        listOfCurrenciesClicked()
    }

    private fun setUpViewModel() {
        repository = repository(this)
        val factory = CurrencyViewModelFactory(repository)
        viewModel = ViewModelProvider(this,factory)[CurrencyViewModel::class.java]
    }


    override fun onNavigationItemSelected(item: MenuItem) =
        when (item.itemId){

            R.id.list_of_currencies -> {
                listOfCurrenciesClicked()
            }
            R.id.list_of_cryptocurrencies -> {
                cryptoCurrenciesClicked()
            }
            R.id.my_currencies -> {
                myCurrenciesClicked()
            }
            else ->false
        }

    private fun cryptoCurrenciesClicked(): Boolean {
        supportFragmentManager.commit {
            replace(R.id.fragment_layout, CryptoFragment())
        }
        return true
    }


    private fun myCurrenciesClicked(): Boolean {
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
