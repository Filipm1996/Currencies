package com.example.currencies2

import RecyclerAdapter2
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.currencies.data.db.Currency
import com.example.currencies.data.db.CurrencyDao
import com.example.currencies.data.db.CurrencyDataBase
import com.example.currencies.data.repositories.repository
import com.example.currencies.databinding.FragmentMyCurrenciesBinding
import com.example.currencies.ui.currencyViewModel
import com.example.currencies.ui.currencyViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@SuppressLint("StaticFieldLeak")
private lateinit var recyclerAdapter: RecyclerAdapter2
private lateinit var viewModel : currencyViewModel
private lateinit var binding: FragmentMyCurrenciesBinding

class MyCurrenciesFragment : Fragment() {

    private lateinit var mContext: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = requireContext()
        val factory = currencyViewModelFactory(repository(mContext))
        viewModel = ViewModelProvider(this, factory).get(currencyViewModel::class.java)
    }

    private fun setOnClickListeners() {
        recyclerAdapter.setOnClickDeleteItem {
            setDeleteAlertDialog(it.name)
        }
    }

    private fun setDeleteAlertDialog(name: String) {
        val builder = AlertDialog.Builder(mContext)
        builder.setMessage("Do you want to delete $name ?")
        builder.setCancelable(false)
        builder.setPositiveButton("Yes") { dialog, _ ->
            deleteCurrency(name)
            Toast.makeText(mContext, "Deleted ${name}", Toast.LENGTH_LONG).show()
        }
        builder.setNegativeButton("No") { dialog, _ ->

        }
        val alert = builder.create()
        alert.show()
    }

    private fun deleteCurrency(name: String) {
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.deleteMyCurrencyByName(name)
        }

        viewModel.getMyCurrencies().observe(viewLifecycleOwner, Observer {
            it.let { recyclerAdapter.setListOfCurrencies(it) }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyCurrenciesBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUpdatedRates().observe(viewLifecycleOwner, Observer {
            recyclerAdapter = RecyclerAdapter2(it,mContext)
            binding.recyclerViewOfList.layoutManager = LinearLayoutManager(mContext)
            binding.recyclerViewOfList.adapter = recyclerAdapter
            setOnClickListeners()
        })

    }


}