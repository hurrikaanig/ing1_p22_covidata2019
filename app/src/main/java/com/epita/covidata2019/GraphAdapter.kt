package com.epita.covidata2019

import android.animation.ObjectAnimator
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GraphAdapter(val context : Activity, val data : List<CasesByCountry>, val disp : Int) : RecyclerView.Adapter<GraphAdapter.Viewholder>() {
    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val InfoView : TextView = itemView.findViewById(R.id.confirmeditem)
        val dateView : TextView = itemView.findViewById(R.id.dateitem)
        val progress : ProgressBar = itemView.findViewById(R.id.progressBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val rowView : View = LayoutInflater.from(context).inflate(R.layout.graph_item, parent, false)
        return Viewholder(rowView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val info : CasesByCountry = data[position]
        holder.dateView.text = info.Date
        holder.progress.max = 100000
        if (disp == 1)
        {
            holder.InfoView.text = info.Confirmed
            ObjectAnimator.ofInt(holder.progress, "progress", info.Confirmed.toInt()).setDuration(1500).start()
        }
        if (disp == 2)
        {
            holder.InfoView.text = info.Deaths
            ObjectAnimator.ofInt(holder.progress, "progress", info.Deaths.toInt()).setDuration(1500).start()
        }
        if (disp == 3)
        {
            holder.InfoView.text = info.Recovered
            ObjectAnimator.ofInt(holder.progress, "progress", info.Deaths.toInt()).setDuration(1500).start()
        }
    }
}