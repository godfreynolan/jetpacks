package com.riis.jetpacketa.features.company.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.riis.jetpacketa.R
import com.riis.jetpacketa.features.company.model.Company

typealias OnItemClicked = (Company) -> Unit

class CompanyRecyclerAdapter(private val companies: List<Company>): RecyclerView.Adapter<CompanyRecyclerAdapter.ViewHolder>() {

    companion object {
        private const val TAG = "CompanyRecyclerAdapter"
    }

    var onItemClicked: OnItemClicked? = null

    // Binds the `Company` data to the elements in the RecyclerView Items
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(company: Company) {
            val nameView = itemView.findViewById<TextView>(R.id.name)
            nameView.text = company.name
            nameView.setOnClickListener { onItemClicked?.invoke(company) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(companies[position])
    }

    override fun getItemCount(): Int {
        return companies.size
    }
}