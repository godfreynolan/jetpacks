package com.riis.jetpacketa.features.company

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.riis.jetpacketa.R
import com.riis.jetpacketa.databinding.FragmentCompanyBinding
import com.riis.jetpacketa.features.company.adapters.CompanyRecyclerAdapter
import com.riis.jetpacketa.features.company.model.Company
import com.riis.jetpacketa.features.route.RoutesFragment

class CompaniesFragment: Fragment() {

    companion object {
        const val TAG = "CompaniesFragment"
    }

    private var _binding: FragmentCompanyBinding? = null
    private val binding get() = _binding!!

    // Add ViewModel
    private val viewModel by viewModels<CompaniesViewModel>()

    // Create an Observer that will automatically update
    // the recycler view when the data changes
    @SuppressLint("NotifyDataSetChanged")
    private val companyListObserver = Observer<List<Company>> {
        companies.clear()
        companies.addAll(it)
        adapter.notifyDataSetChanged()
    }

    // Setup RecyclerView
    private lateinit var adapter: CompanyRecyclerAdapter
    private var companies: MutableList<Company> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompanyBinding.inflate(inflater, container, false)

        // Fetch the company list
        viewModel.getCompanies()

        // Update Top Toolbar and disable the `Back` button
        (activity as AppCompatActivity).supportActionBar?.title = "Companies"
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        adapter = CompanyRecyclerAdapter(companies).apply {
            onItemClicked = {
                // Item Clicked, start new fragment
                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frameLayout, RoutesFragment::class.java, bundleOf("companyId" to it.id, "companyName" to it.name), RoutesFragment.TAG)
                    .addToBackStack(RoutesFragment.TAG)
                    .commit()
            }
        }
        // Setup RecyclerView layout manager, decorations, and adapter
        binding.companyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.companyRecyclerView.adapter = adapter

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // When fragment resumes, start observer
        viewModel.companies.observe(this, companyListObserver)
    }

    override fun onStop() {
        super.onStop()
        // When fragment stops, remove the live data observer
        viewModel.companies.removeObserver(companyListObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}