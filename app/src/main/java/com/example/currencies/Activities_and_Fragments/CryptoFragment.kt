package com.example.currencies.Activities_and_Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.currencies.adapters.RecyclerAdapterForCyrpto
import com.example.currencies.data.repositories.repository
import com.example.currencies.databinding.FragmentCryptoBinding
import com.example.currencies.ui.currencyViewModel
import com.example.currencies.ui.currencyViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CryptoFragment : Fragment() {
    private lateinit var mContext: Context
    private lateinit var binding : FragmentCryptoBinding
    private lateinit var viewModel : currencyViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext =requireContext()
        val factory = currencyViewModelFactory(repository(mContext))
        viewModel = ViewModelProvider(this,factory)[currencyViewModel::class.java]
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCryptoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CoroutineScope(Dispatchers.IO).launch {
            val listOfCruptocurrecies = viewModel.getRecordsFromNomics()
            withContext(Dispatchers.Main){
                val lifecycleOwner = viewLifecycleOwner
                binding.recyclerViewForCrypto.layoutManager = LinearLayoutManager(mContext)
                val recyclerAdapter = RecyclerAdapterForCyrpto(mContext,lifecycleOwner,listOfCruptocurrecies)
                binding.recyclerViewForCrypto.adapter = recyclerAdapter
            }
        }
    }
}