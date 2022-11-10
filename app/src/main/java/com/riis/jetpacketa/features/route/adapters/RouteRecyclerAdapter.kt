package com.riis.jetpacketa.features.route.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.riis.jetpacketa.databinding.RecyclerViewItemBinding
import com.riis.jetpacketa.features.route.room.Route

typealias OnItemClicked = (Route) -> Unit

class RouteRecyclerAdapter(private val routes: List<Route>): RecyclerView.Adapter<RouteRecyclerAdapter.ViewHolder>() {

    companion object {
        private const val TAG = "CompanyRecyclerAdapter"
    }

    var onItemClicked: OnItemClicked? = null

    inner class ViewHolder(private val binding: RecyclerViewItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(route: Route) {
            binding.name.text = route.routeLongName
            binding.name.setOnClickListener { onItemClicked?.invoke(route) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(routes[position])
    }

    override fun getItemCount(): Int {
        return routes.size
    }
}