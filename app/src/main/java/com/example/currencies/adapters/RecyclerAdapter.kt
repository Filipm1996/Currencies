package com.example.currencies.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.currencies.data.db.Currency
import com.example.currencies.R

class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.ViewHolder> () {
    private var dataSource: List<Currency>? = null
    private var onAddButtonClick : ((Currency)->Unit)? = null
    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.title)
        var value: TextView = view.findViewById(R.id.value)
        var addButton : ImageButton = view.findViewById(R.id.addButton)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view =LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_element, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return dataSource!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currency = dataSource!![position]
        holder.title.text = currency.name
        holder.value.text = currency.rate
        holder.addButton.setOnClickListener{
            onAddButtonClick?.invoke(currency)
        }
    }

    fun setOnAddButtonClick(callback : (Currency)->Unit){
        this.onAddButtonClick = callback
    }

    fun addList( list : List<Currency>){
        this.dataSource = list
    }

}

