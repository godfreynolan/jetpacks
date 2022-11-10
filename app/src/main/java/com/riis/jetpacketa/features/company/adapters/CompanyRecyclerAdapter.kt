package com.riis.jetpacketa.features.company.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.riis.jetpacketa.databinding.RecyclerViewItemBinding
import com.riis.jetpacketa.features.company.model.Company

typealias OnItemClicked = (Company) -> Unit

class CompanyRecyclerAdapter(private val companies: List<Company>): RecyclerView.Adapter<CompanyRecyclerAdapter.ViewHolder>() {

    companion object {
        private const val TAG = "CompanyRecyclerAdapter"
    }

    var onItemClicked: OnItemClicked? = null

    inner class ViewHolder(private val binding: RecyclerViewItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(company: Company) {
            binding.name.text = company.name
            binding.name.setOnClickListener { onItemClicked?.invoke(company) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(companies[position])
    }

    override fun getItemCount(): Int {
        return companies.size
    }
}