package com.epita.covidata2019

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DateAdapter(val context : Activity, val data : List<CasesByCountry>, val onItemClickListener: View.OnClickListener) : RecyclerView.Adapter<DateAdapter.ViewHolder>(){
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val nameDate : TextView = itemView.findViewById(R.id.list_item_country_txt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rowView : View = LayoutInflater.from(context).inflate(R.layout.list_item_country, parent, false)
        rowView.setOnClickListener(onItemClickListener)
        return ViewHolder(rowView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val date : CasesByCountry = data[position]
        holder.nameDate.text = date.Date
        holder.itemView.tag = position
    }
}