package com.example.currencies.adapters


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.currencies.R
import com.example.currencies.data.db.Currency


class RecyclerAdapterForCyrpto : RecyclerView.Adapter<RecyclerAdapterForCyrpto.ViewHolder>() {
    private var listOfCryptocurrencies : List<Currency>? = null
    private var listOfCurrencies : List<Currency>? = null
    private var usdRate : String? =null
    private var onAddButtonClick : ((Currency)->Unit)? = null
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.title)
        var value: TextView = view.findViewById(R.id.value)
        var addButton : ImageButton = view.findViewById(R.id.addButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_element, parent,false)
        return ViewHolder(view)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cryptoCurrency = listOfCryptocurrencies?.get(position)
            val USDwithDot = usdRate!!.replace(",", ".")
            val priceOfCurrency = cryptoCurrency!!.rate.replace(",", ".").toDouble() * USDwithDot.toDouble()
            val cryptoPLN = Currency(cryptoCurrency.name, priceOfCurrency.toString(), "crypto")
            holder.value.text = String.format("%.4f", priceOfCurrency)
            holder.addButton.setOnClickListener {
                onAddButtonClick?.invoke(cryptoPLN)
            }
            holder.title.text = cryptoCurrency.name
    }


    override fun getItemCount(): Int {
        return listOfCryptocurrencies?.size ?: 0
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addList(list:List<Currency>?){
        this.listOfCurrencies = list
        listOfCryptocurrencies = listOfCurrencies?.filter { it.typeOfCurrency =="crypto" } ?: listOf()
        usdRate = listOfCurrencies?.find { it.name =="dolar amerykański" }?.rate ?: "4.4"
        notifyDataSetChanged()
    }

    fun setOnAddButtonClickListener(callback : (Currency) -> Unit){
        this.onAddButtonClick = callback
        notifyDataSetChanged()
    }
}

