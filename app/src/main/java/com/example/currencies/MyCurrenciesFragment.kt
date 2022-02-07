package com.example.currencies2

import RecyclerAdapter2
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.currencies.RoomDataBase.Currency
import com.example.currencies.RoomDataBase.CurrencyDao
import com.example.currencies.RoomDataBase.CurrencyDataBase
import com.example.currencies.databinding.FragmentMyCurrenciesBinding
import kotlinx.coroutines.*

private lateinit var recyclerAdapter: RecyclerAdapter2
private lateinit var database : CurrencyDataBase
private lateinit var database2 : CurrencyDataBase
private lateinit var dao : CurrencyDao
private lateinit var dao2 : CurrencyDao
private lateinit var binding: FragmentMyCurrenciesBinding

class MyCurrenciesFragment : Fragment() {

    private lateinit var mContext : Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = requireContext()

    }

    suspend private fun setOnClickListeners() {
            recyclerAdapter.setOnClickDeleteItem {
                GlobalScope.launch(Dispatchers.Main) {
                setDeleteAlertDialog(it.name)
            }
        }
    }

    suspend private fun setDeleteAlertDialog(name:String) {
        GlobalScope.launch(Dispatchers.Main) {
            val builder = AlertDialog.Builder(mContext)
            builder.setMessage("Do you want to delete $name ?")
            builder.setCancelable(false)
            builder.setPositiveButton("Yes") { dialog, _ ->
                GlobalScope.launch(Dispatchers.Main) {
                    deleteCurrency(name)
                    Toast.makeText(mContext, "Deleted ${name}", Toast.LENGTH_LONG).show()
                }
            }
            builder.setNegativeButton("No") { dialog, _ ->

            }
            val alert = builder.create()
            alert.show()
        }
    }

    suspend private fun deleteCurrency(name: String) {
        CoroutineScope(Dispatchers.Main).launch {
            dao.deleteCurrencyByName(name)
            dao.getAllCurrencies().let { recyclerAdapter.setListOfCurrencies(it) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyCurrenciesBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
        GlobalScope.launch(Dispatchers.IO) {
            database = Room.databaseBuilder(mContext, CurrencyDataBase::class.java, "Mycurrency_database").fallbackToDestructiveMigration().build()
            database2 = Room.databaseBuilder(mContext, CurrencyDataBase::class.java, "currency_database").fallbackToDestructiveMigration().build()
            dao = database.currencyDao()
            dao2 = database.currencyDao()
            var listOfMyCurrencies = dao.getAllCurrencies()
            val listOfCurrencies = dao2.getAllCurrencies()
            recyclerAdapter = RecyclerAdapter2(updateData(listOfMyCurrencies,listOfCurrencies))
            withContext(Dispatchers.Main) {
                binding.recyclerViewOfList.layoutManager = LinearLayoutManager(mContext)
                binding.recyclerViewOfList.adapter = recyclerAdapter
                setOnClickListeners()
            }
        }


}

    private fun updateData(myList : List<Currency>, list : List<Currency>) : List<Currency>{
        if(myList.isNotEmpty()) {
            val sizeOfMyList : Int = myList.size -1
            for (i in 0..sizeOfMyList){
                var listIndex = list.indexOf(myList[i])
                myList[i].rate = list[listIndex].rate
            }
            return myList
        }
        else return listOf()
    }

}