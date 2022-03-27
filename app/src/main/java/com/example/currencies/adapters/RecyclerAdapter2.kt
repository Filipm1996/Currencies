package com.example.currencies.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.currencies.data.db.Currency
import com.example.currencies.R
import com.example.currencies.data.repositories.repository


class RecyclerAdapter2(
    private var dataSource: List<Currency>,
    var mContext : Context
): RecyclerView.Adapter<RecyclerAdapter2.ViewHolder> (){
    private lateinit var repository: repository
    var onClickDeleteItem :((Currency)->Unit)? = null
    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.title)
        var value: TextView = view.findViewById(R.id.value)
        var delete: ImageButton = view.findViewById(R.id.deleteButton)



    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        repository= repository(mContext)
        val view =LayoutInflater.from(parent.context).inflate(R.layout.my_currencies_element, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return dataSource.size
    }

        @JvmName("setOnClickDeleteItem1")
        fun setOnClickDeleteItem(callback:(Currency)-> Unit){
            this.onClickDeleteItem = callback
        }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var currency = dataSource[position]
        holder.title.text = currency.name
        holder.value.text = currency.rate
        holder.delete.setOnClickListener{
            onClickDeleteItem?.invoke(currency)}
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setListOfCurrencies(array1 : List<Currency>) {
        this.dataSource = array1
        notifyDataSetChanged()
    }
}