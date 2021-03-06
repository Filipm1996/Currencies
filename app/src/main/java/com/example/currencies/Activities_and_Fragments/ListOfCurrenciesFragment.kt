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
import com.example.currencies.ui.CurrencyViewModel
import com.example.currencies.ui.CurrencyViewModelFactory


class ListOfCurrenciesFragment : Fragment(), LifecycleObserver{
    private var recyclerAdapter: RecyclerAdapter? = null
    private lateinit var mContext : Context
    private lateinit var viewModel : CurrencyViewModel
    private lateinit var binding : FragmentListOfCurrenciesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = requireContext()
        recyclerAdapter = RecyclerAdapter()
        viewModel = ViewModelProviders.of(requireActivity())[CurrencyViewModel::class.java]
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
        setUpClickListeners()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllCurrencies().observe(viewLifecycleOwner, Observer {
            recyclerAdapter!!.addList(it.filter { it.typeOfCurrency == "normal" })
            binding.recyclerViewOfList.layoutManager = LinearLayoutManager(mContext)
            binding.recyclerViewOfList.adapter = recyclerAdapter
        })
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


