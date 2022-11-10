package com.riis.jetpacketa.features.company

import android.annotation.SuppressLint
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.riis.jetpacketa.R
import com.riis.jetpacketa.database.SqliteHelper
import com.riis.jetpacketa.features.company.adapters.CompanyRecyclerAdapter
import com.riis.jetpacketa.features.company.model.Company
import com.riis.jetpacketa.features.route.RoutesFragment
import java.util.concurrent.Executors
import java.util.concurrent.Future

class CompaniesFragment: Fragment() {

    companion object {
        const val TAG = "CompaniesFragment"
    }

    // Setup RecyclerView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CompanyRecyclerAdapter
    private var companies: MutableList<Company> = mutableListOf()
    private var executor = Executors.newSingleThreadExecutor()
    private lateinit var futureRunnable: Future<*>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_company, container, false)
        Log.i(TAG, "onCreateView: Inflating")

        (activity as AppCompatActivity).supportActionBar?.title = "Companies"
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        // Attach Adapter and Temporary Data to RecyclerView
        recyclerView = view.findViewById(R.id.companyRecyclerView)

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
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        recyclerView.adapter = adapter

        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    private val companiesQueryRunnable = Runnable {
        val dbName = "gtfs_room.db"
        val dbInputStream = requireActivity().applicationContext.assets.open(dbName)
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
}