package com.example.currencies2


import RecyclerAdapter
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.currencies.data.db.Currency
import com.example.currencies.data.repositories.repository
import com.example.currencies.databinding.FragmentListOfCurrenciesBinding
import com.example.currencies.ui.currencyViewModel
import com.example.currencies.ui.currencyViewModelFactory


class ListOfCurrenciesFragment : Fragment(), LifecycleObserver{
    private lateinit var recyclerAdapter: RecyclerAdapter
    private lateinit var mContext : Context
    private lateinit var viewModel : currencyViewModel
    private lateinit var binding : FragmentListOfCurrenciesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = requireContext()
        val factory = currencyViewModelFactory(repository(mContext))
        viewModel = ViewModelProvider(this,factory)[currencyViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListOfCurrenciesBinding.inflate(inflater,container,false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var listOfCurrencies: List<Currency>
        viewModel.getAllCurrencies().observe(viewLifecycleOwner, Observer {
            listOfCurrencies = it
            val lifecycleOwner = viewLifecycleOwner
            binding.recyclerViewOfList.layoutManager = LinearLayoutManager(mContext)
            recyclerAdapter = RecyclerAdapter(listOfCurrencies, mContext, lifecycleOwner)
            binding.recyclerViewOfList.adapter = recyclerAdapter
            })
        }
    }


