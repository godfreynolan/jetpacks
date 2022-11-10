package com.riis.jetpacketa.features.stop.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.riis.jetpacketa.R
import com.riis.jetpacketa.features.stop.model.Stop
import com.riis.jetpacketa.features.stop.model.StopUi

typealias OnItemClicked = (StopUi) -> Unit

class StopRecyclerAdapter(private val stops: List<StopUi>): RecyclerView.Adapter<StopRecyclerAdapter.ViewHolder>() {

    companion object {
        private const val TAG = "CompanyRecyclerAdapter"
    }

    var onItemClicked: OnItemClicked? = null

    // Binds the `Company` data to the elements in the RecyclerView Items
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(stop: StopUi) {
            val nameView = itemView.findViewById<TextView>(R.id.name)
            nameView.text = stop.stopName
            nameView.setOnClickListener { onItemClicked?.invoke(stop) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(stops[position])
    }

    override fun getItemCount(): Int {
        return stops.size
    }
}