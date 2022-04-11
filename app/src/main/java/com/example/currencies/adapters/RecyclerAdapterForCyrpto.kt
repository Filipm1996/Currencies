package com.example.currencies.adapters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.currencies.R
import com.example.currencies.data.db.Currency
import com.example.currencies.data.repositories.repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecyclerAdapterForCyrpto(
    private val mContext : Context,
    private val lifecycleOwner: LifecycleOwner,
    private val listOfCryptocurrencies : List<Currency>
) : RecyclerView.Adapter<RecyclerAdapterForCyrpto.ViewHolder>() {
    private lateinit var repository: repository
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.title)
        var value: TextView = view.findViewById(R.id.value)
        var addButton : ImageButton = view.findViewById(R.id.addButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        repository = repository(mContext)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_element, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cryptoCurrency = listOfCryptocurrencies[position]
        repository.getAllCurrencies().observe(lifecycleOwner, Observer { list ->
            val USD = list.find { it.name == "dolar amerykański" }
            val USDwithDot = USD!!.rate.replace(",", ".")
            val priceOfCurrency = cryptoCurrency.rate.toDouble()*USDwithDot.toDouble()
            val cryptoPLN = Currency(cryptoCurrency.name,priceOfCurrency.toString(), "crypto")
            holder.value.text = String.format("%.4f", priceOfCurrency)
            holder.addButton.setOnClickListener {
                setAlertDialog(cryptoPLN)
            }
        })
        holder.title.text = cryptoCurrency.name
    }

    private fun setAlertDialog(cryptoCurrency: Currency) {
        val buildier = AlertDialog.Builder(mContext)
        buildier.setMessage("Do you want to add ${cryptoCurrency.name}?")
        buildier.setCancelable(true)
        buildier.setPositiveButton("yes") { dialog, _ ->

            repository.getMyAllCurrencies().observeOnce(lifecycleOwner) {
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
                            repository.insertMyCurrency(cryptoItem)
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

    override fun getItemCount(): Int {
        return listOfCryptocurrencies.size
    }
}

