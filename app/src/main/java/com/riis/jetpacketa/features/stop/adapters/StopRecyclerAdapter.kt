package com.riis.jetpacketa.features.stop.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.riis.jetpacketa.R
import com.riis.jetpacketa.databinding.RecyclerViewItemBinding
import com.riis.jetpacketa.features.stop.room.StopUi

typealias OnItemClicked = (StopUi, Boolean, Int) -> Unit

class StopRecyclerAdapter(private val stops: List<StopUi>): RecyclerView.Adapter<StopRecyclerAdapter.ViewHolder>() {

    companion object {
        private const val TAG = "CompanyRecyclerAdapter"
    }

    var onItemClicked: OnItemClicked? = null

    // Binds the `Company` data to the elements in the RecyclerView Items
    inner class ViewHolder(private val binding: RecyclerViewItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(stop: StopUi, position: Int) {
            binding.name.text = stop.stopName
            binding.favoriteImageView.visibility = View.VISIBLE
            binding.favoriteImageView.setImageResource(R.drawable.ic_baseline_star_outline_24)
            if(stop.favorite) binding.favoriteImageView.setImageResource(R.drawable.ic_baseline_star_24)

            binding.favoriteImageView.setOnClickListener { onItemClicked?.invoke(stop, !stop.favorite, position) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(stops[position], position)
    }

    override fun getItemCount(): Int {
        return stops.size
    }
}