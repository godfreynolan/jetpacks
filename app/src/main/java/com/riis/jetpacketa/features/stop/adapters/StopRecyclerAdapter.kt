package com.riis.jetpacketa.features.stop.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.riis.jetpacketa.databinding.RecyclerViewItemBinding
import com.riis.jetpacketa.features.stop.model.StopUi

typealias OnItemClicked = (StopUi) -> Unit

class StopRecyclerAdapter(private val stops: List<StopUi>): RecyclerView.Adapter<StopRecyclerAdapter.ViewHolder>() {

    companion object {
        private const val TAG = "CompanyRecyclerAdapter"
    }

    var onItemClicked: OnItemClicked? = null

    // Binds the `Company` data to the elements in the RecyclerView Items
    inner class ViewHolder(private val binding: RecyclerViewItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(stop: StopUi) {
            binding.name.text = stop.stopName
            binding.name.setOnClickListener { onItemClicked?.invoke(stop) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(stops[position])
    }

    override fun getItemCount(): Int {
        return stops.size
    }
}