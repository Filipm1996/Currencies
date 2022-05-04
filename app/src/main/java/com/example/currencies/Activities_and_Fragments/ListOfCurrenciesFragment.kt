package com.example.currencies.Activities_and_Fragments


import android.app.AlertDialog
import com.example.currencies.adapters.RecyclerAdapter
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.currencies.data.db.Currency
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
            setAlertDialog(it)
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
    fun setAlertDialog(currency: Currency) {

        val buildier = AlertDialog.Builder(mContext)
        buildier.setMessage("Do you want to add ${currency.name}?")
        buildier.setCancelable(true)
        buildier.setPositiveButton("yes") { dialog, _ ->

            viewModel.getMyCurrencies().observeOnce(viewLifecycleOwner) {
                if (it != null) {
                    val myList = it
                    if (myList.contains(currency)) {

                        Toast.makeText(
                            mContext,
                            "${currency.name} is in favourites",
                            Toast.LENGTH_LONG
                        ).show()

                    } else {
                        viewModel.insertMyCurrency(currency)
                        Toast.makeText(
                            mContext,
                            "Added ${currency.name}",
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
    fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }
    }


