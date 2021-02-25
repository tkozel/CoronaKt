package cz.uhk.coronakt.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cz.uhk.coronakt.R
import cz.uhk.coronakt.model.CovidData
import java.text.DateFormat

class CovidDataAdapter(private val covidData : CovidData) : RecyclerView.Adapter<CovidDataAdapter.ViewHolder>() {
    class ViewHolder(view: View ) : RecyclerView.ViewHolder(view) {
        val tvDate : TextView = view.findViewById(R.id.item_date)
        val tvPcr : TextView = view.findViewById(R.id.item_pcr)
        val tvAg : TextView = view.findViewById(R.id.item_ag)
        val tvPositive : TextView = view.findViewById(R.id.item_positive)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.stats_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = covidData.data[position]
        holder.tvDate.text = DateFormat.getDateInstance().format(item.day)
        holder.tvPcr.text = item.pcrCnt.toString()
        holder.tvAg.text = item.ag.toString()
        holder.tvPositive.text = item.positive.toString()
        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(Color.rgb(240,240,240))
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE)
        }
    }

    override fun getItemCount(): Int {
        return covidData.data.size
    }
}