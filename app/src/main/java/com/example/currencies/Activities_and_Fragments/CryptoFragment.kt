package com.example.currencies.Activities_and_Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.currencies.adapters.RecyclerAdapterForCyrpto
import com.example.currencies.data.repositories.repository
import com.example.currencies.databinding.FragmentCryptoBinding
import com.example.currencies.ui.currencyViewModel
import com.example.currencies.ui.currencyViewModelFactory



class CryptoFragment : Fragment() {
    private var recyclerAdapter: RecyclerAdapterForCyrpto? = null
    private lateinit var  lifecycleOwner :LifecycleOwner
    private lateinit var mContext: Context
    private lateinit var binding : FragmentCryptoBinding
    private lateinit var viewModel : currencyViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext =requireContext()
        recyclerAdapter = RecyclerAdapterForCyrpto()
        val factory = currencyViewModelFactory(repository(mContext))
        viewModel = ViewModelProvider(this,factory)[currencyViewModel::class.java]
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lifecycleOwner = viewLifecycleOwner
        binding = FragmentCryptoBinding.inflate(layoutInflater)
        viewModel.getAllCurrencies().observe(lifecycleOwner) {
            binding.recyclerViewForCrypto.layoutManager = LinearLayoutManager(mContext)
            recyclerAdapter!!.addList(it)
            setUpClickListeners()
            binding.recyclerViewForCrypto.adapter = recyclerAdapter

        }
        return binding.root
    }

    fun setUpClickListeners(){
        recyclerAdapter!!.setOnAddButtonClickListener {
            viewModel.setAddButtonAlertDialog(it,mContext,lifecycleOwner)
        }
    }
}