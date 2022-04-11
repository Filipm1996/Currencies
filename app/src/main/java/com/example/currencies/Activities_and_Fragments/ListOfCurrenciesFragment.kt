package com.example.currencies.Activities_and_Fragments


import com.example.currencies.adapters.RecyclerAdapter
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.currencies.data.repositories.repository
import com.example.currencies.databinding.FragmentListOfCurrenciesBinding
import com.example.currencies.ui.currencyViewModel
import com.example.currencies.ui.currencyViewModelFactory


class ListOfCurrenciesFragment : Fragment(), LifecycleObserver{
    private var recyclerAdapter: RecyclerAdapter? = null
    private lateinit var mContext : Context
    private lateinit var viewModel : currencyViewModel
    private lateinit var binding : FragmentListOfCurrenciesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = requireContext()
        recyclerAdapter = RecyclerAdapter()
        val factory = currencyViewModelFactory(repository(mContext))
        viewModel = ViewModelProvider(this,factory)[currencyViewModel::class.java]
        }

    private fun setUpClickListeners() {
        recyclerAdapter!!.setOnAddButtonClick {
            viewModel.setAlertDialog(it,mContext,viewLifecycleOwner)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListOfCurrenciesBinding.inflate(inflater,container,false)
        viewModel.getAllCurrencies().observe(viewLifecycleOwner, Observer {
            recyclerAdapter!!.addList(it)
            binding.recyclerViewOfList.layoutManager = LinearLayoutManager(mContext)
            binding.recyclerViewOfList.adapter = recyclerAdapter
        })
        setUpClickListeners()
        return binding.root
    }

    }


