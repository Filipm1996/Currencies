package com.example.currencies.Activities_and_Fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.currencies.adapters.RecyclerAdapterForCyrpto
import com.example.currencies.data.db.Currency
import com.example.currencies.data.repositories.repository
import com.example.currencies.databinding.FragmentCryptoBinding
import com.example.currencies.ui.CurrencyViewModel
import com.example.currencies.ui.CurrencyViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CryptoFragment : Fragment() {
    private var recyclerAdapter: RecyclerAdapterForCyrpto? = null
    private lateinit var  lifecycleOwner :LifecycleOwner
    private lateinit var mContext: Context
    private lateinit var binding : FragmentCryptoBinding
    private lateinit var viewModel : CurrencyViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext =requireContext()
        recyclerAdapter = RecyclerAdapterForCyrpto()
        viewModel = ViewModelProviders.of(requireActivity())[CurrencyViewModel::class.java]
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lifecycleOwner = viewLifecycleOwner
        binding = FragmentCryptoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllCurrencies().observe(lifecycleOwner) {
            binding.recyclerViewForCrypto.layoutManager = LinearLayoutManager(mContext)
            recyclerAdapter!!.addList(it)
            setUpClickListeners()
            binding.recyclerViewForCrypto.adapter = recyclerAdapter

        }
    }
    fun setUpClickListeners(){
        recyclerAdapter!!.setOnAddButtonClickListener {
            setAddButtonAlertDialog(it)
        }
    }

    fun setAddButtonAlertDialog(cryptoCurrency: Currency) {
        val buildier = AlertDialog.Builder(mContext)
        buildier.setMessage("Do you want to add ${cryptoCurrency.name}?")
        buildier.setCancelable(true)
        buildier.setPositiveButton("yes") { dialog, _ ->

            viewModel.getMyCurrencies().observe(lifecycleOwner) {
                if (it != null) {
                    val myList = it
                    val formattedPrice = String.format("%.4f", cryptoCurrency.rate.toDouble())
                    val cryptoItem = Currency(cryptoCurrency.name, formattedPrice, "crypto")
                    var isIn = false
                    for (i in myList) {
                        if (i.name == cryptoCurrency.name) {
                            isIn = true
                        }
                    }
                    if (isIn) {
                        Toast.makeText(
                            mContext,
                            "${cryptoCurrency.name} is in favourites",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        CoroutineScope(Dispatchers.IO).launch {
                            viewModel.insertMyCurrency(cryptoCurrency)
                        }
                        Toast.makeText(
                            mContext,
                            "Added ${cryptoItem.name}",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }
            }
        }
        buildier.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val alert = buildier.create()
        alert.show()

    }

}