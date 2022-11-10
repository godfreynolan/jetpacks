package com.riis.jetpacketa.features.company

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.riis.jetpacketa.R
import com.riis.jetpacketa.database.SqliteHelper
import com.riis.jetpacketa.databinding.FragmentCompanyBinding
import com.riis.jetpacketa.features.company.adapters.CompanyRecyclerAdapter
import com.riis.jetpacketa.features.company.model.Company
import com.riis.jetpacketa.features.route.RoutesFragment
import java.util.concurrent.Executors
import java.util.concurrent.Future

class CompaniesFragment: Fragment() {

    companion object {
        const val TAG = "CompaniesFragment"
    }

    private var _binding: FragmentCompanyBinding? = null
    private val binding get() = _binding!!

    // Setup RecyclerView
    private lateinit var adapter: CompanyRecyclerAdapter
    private var companies: MutableList<Company> = mutableListOf()
    private var executor = Executors.newSingleThreadExecutor()
    private lateinit var futureRunnable: Future<*>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompanyBinding.inflate(inflater, container, false)

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
        binding.companyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.companyRecyclerView.adapter = adapter

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private val companiesQueryRunnable = Runnable {
        val dbName = "jetpacketa.db"
        val dbInputStream = requireContext().applicationContext.assets.open("jetpacketa.db")

        val helper = SqliteHelper.getInstance(dbInputStream, dbName)
        val newCompanies = helper.getCompanies()

        requireActivity().runOnUiThread {
            companies.clear()
            companies.addAll(newCompanies)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onResume() {
        super.onResume()
        futureRunnable = executor.submit(companiesQueryRunnable)
    }

    override fun onStop() {
        super.onStop()
        futureRunnable.cancel(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}