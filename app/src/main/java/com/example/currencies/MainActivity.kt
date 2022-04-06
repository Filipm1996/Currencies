package com.example.currencies



import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.android.volley.toolbox.DiskBasedCache
import com.example.currencies.data.repositories.repository
import com.example.currencies.databinding.ActivityMainBinding
import com.example.currencies.ui.currencyViewModel
import com.example.currencies.ui.currencyViewModelFactory
import com.example.currencies2.ListOfCurrenciesFragment
import com.google.android.material.navigation.NavigationBarView


class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {

    private lateinit var viewModel : currencyViewModel
    private lateinit var repository: repository
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNav.setOnItemSelectedListener(this)
        setUpViewModel()
        myCurrenciesClicked()
        viewModel.gettingJsonString("https://api.nbp.pl/api/exchangerates/tables/A/?format=json",DiskBasedCache(cacheDir, 1024* 1024))
    }

    private fun setUpViewModel() {
        repository = repository(this)
        val factory = currencyViewModelFactory(repository)
        viewModel = ViewModelProvider(this,factory)[currencyViewModel::class.java]
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
