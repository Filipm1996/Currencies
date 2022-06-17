package com.example.currencies.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.currencies.data.db.Currency
import com.example.currencies.R



class RecyclerAdapter2 : RecyclerView.Adapter<RecyclerAdapter2.ViewHolder> (){
    private var dataSource: List<Currency>? =null
    private var onClickDeleteItem :((Currency)->Unit)? = null
    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.title)
        var value: TextView = view.findViewById(R.id.value)
        var delete: ImageButton = view.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view =LayoutInflater.from(parent.context).inflate(R.layout.my_currencies_element, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return dataSource?.size ?: 0
    }

        @JvmName("setOnClickDeleteItem1")
        fun setOnClickDeleteItem(callback:(Currency)-> Unit){
            this.onClickDeleteItem = callback
        }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currency = dataSource!![position]
        holder.title.text = currency.name
        holder.value.text = String.format("%.4f", currency.rate.toDouble())
        holder.delete.setOnClickListener{
            onClickDeleteItem?.invoke(currency)}
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setListOfCurrencies(array1 : List<Currency>?) {
        this.dataSource = array1 ?: listOf()
        notifyDataSetChanged()
    }
}