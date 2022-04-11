package com.example.currencies.Activities_and_Fragments

import com.example.currencies.adapters.RecyclerAdapter2
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.currencies.data.repositories.repository
import com.example.currencies.databinding.FragmentMyCurrenciesBinding
import com.example.currencies.ui.currencyViewModel
import com.example.currencies.ui.currencyViewModelFactory



private lateinit var viewModel : currencyViewModel
private lateinit var binding: FragmentMyCurrenciesBinding

class MyCurrenciesFragment : Fragment() {
    private var recyclerAdapter : RecyclerAdapter2? = null
    private lateinit var mContext: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = requireContext()
        val factory = currencyViewModelFactory(repository(mContext))
        viewModel = ViewModelProvider(this, factory)[currencyViewModel::class.java]
    }

    private fun setOnClickListeners() {
        recyclerAdapter!!.setOnClickDeleteItem {
            setDeleteAlertDialog(it.name)
        }
    }

    private fun setDeleteAlertDialog(name: String) {
        val builder = AlertDialog.Builder(mContext)
        builder.setMessage("Do you want to delete $name ?")
        builder.setCancelable(false)
        builder.setPositiveButton("Yes") { _, _ ->
            deleteCurrency(name)
            Toast.makeText(mContext, "Deleted $name", Toast.LENGTH_LONG).show()
        }
        builder.setNegativeButton("No") { _, _ ->

        }
        val alert = builder.create()
        alert.show()
    }

    private fun deleteCurrency(name: String) {

        viewModel.deleteMyCurrencyByName(name)

        viewModel.getMyCurrencies().observe(viewLifecycleOwner) {
            it.let { recyclerAdapter!!.setListOfCurrencies(it) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyCurrenciesBinding.inflate(inflater, container, false)
        with(viewModel) {
            getUpdatedRates().observe(
                viewLifecycleOwner
            ) {
                recyclerAdapter = RecyclerAdapter2()
                recyclerAdapter!!.setListOfCurrencies(it)
                setOnClickListeners()
                binding.recyclerViewOfList.layoutManager = LinearLayoutManager(mContext)
                binding.recyclerViewOfList.adapter = recyclerAdapter
            }
        }
        return binding.root
    }

}