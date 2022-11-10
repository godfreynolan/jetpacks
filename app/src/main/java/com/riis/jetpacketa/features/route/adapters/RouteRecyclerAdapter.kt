package com.riis.jetpacketa.features.route.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.riis.jetpacketa.R
import com.riis.jetpacketa.features.route.model.Route

typealias OnItemClicked = (Route) -> Unit

class RouteRecyclerAdapter(private val routes: List<Route>): RecyclerView.Adapter<RouteRecyclerAdapter.ViewHolder>() {

    companion object {
        private const val TAG = "CompanyRecyclerAdapter"
    }

    var onItemClicked: OnItemClicked? = null

    // Binds the `Company` data to the elements in the RecyclerView Items
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(route: Route) {
            val nameView = itemView.findViewById<TextView>(R.id.name)
            nameView.text = route.routeLongName
            nameView.setOnClickListener { onItemClicked?.invoke(route) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(routes[position])
    }

    override fun getItemCount(): Int {
        return routes.size
    }
}