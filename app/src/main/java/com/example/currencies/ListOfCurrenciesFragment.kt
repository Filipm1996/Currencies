package com.example.currencies2


import DataBaseHelper
import RecyclerAdapter
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.currencies.R
import com.example.currencies.RoomDataBase.CurrencyDao
import com.example.currencies.RoomDataBase.CurrencyDataBase
import com.example.currencies.databinding.FragmentListOfCurrenciesBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class ListOfCurrenciesFragment : Fragment() {
    private lateinit var recyclerAdapter: RecyclerAdapter
    private lateinit var mContext : Context
    private lateinit var database : CurrencyDataBase
    private lateinit var dao : CurrencyDao
    private lateinit var binding : FragmentListOfCurrenciesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = requireContext()
        GlobalScope.launch (Dispatchers.Main) {
            database = Room.databaseBuilder(mContext, CurrencyDataBase::class.java, "currency_database").fallbackToDestructiveMigration().build()
            dao = database.currencyDao()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListOfCurrenciesBinding.inflate(inflater,container,false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GlobalScope.launch(Dispatchers.Main) {
            val listOfCurrencies = dao.getAllCurrencies()
            binding.recyclerViewOfList.layoutManager = LinearLayoutManager(mContext)
            recyclerAdapter = RecyclerAdapter(listOfCurrencies, mContext)
            binding.recyclerViewOfList.adapter = recyclerAdapter
        }

        }
    }


