package com.riis.jetpacketa.features.route

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
import androidx.recyclerview.widget.RecyclerView
import com.riis.jetpacketa.R
import com.riis.jetpacketa.database.SqliteHelper
import com.riis.jetpacketa.features.route.adapters.RouteRecyclerAdapter
import com.riis.jetpacketa.features.route.model.Route
import com.riis.jetpacketa.features.stop.StopsFragment
import java.util.concurrent.Executors
import java.util.concurrent.Future

class RoutesFragment: Fragment() {

    companion object {
        const val TAG = "RouteFragment"
    }

    private lateinit var adapter: RouteRecyclerAdapter
    private lateinit var recyclerView: RecyclerView
    private var executor = Executors.newSingleThreadExecutor()
    private lateinit var futureRunnable: Future<*>
    private val routes: MutableList<Route> = mutableListOf()
    private var companyId: Int = -1
    private var companyName: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_route, container, false)
        companyId = arguments?.getInt("companyId") ?: -1
        companyName = arguments?.getString("companyName", "") ?: ""

        if(companyName == "Detroit Department of Transportation") companyName = "DDOT"

        (activity as AppCompatActivity).supportActionBar?.title = "$companyName Routes"
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Attach Adapter and Temporary Data to RecyclerView
        recyclerView = view.findViewById(R.id.routeRecyclerView)

        adapter = RouteRecyclerAdapter(routes).apply {
            onItemClicked = {
                // Item Clicked, start new fragment
                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(
                        R.id.frameLayout,
                        StopsFragment::class.java,
                        bundleOf(
                            "companyId" to companyId,
                            "companyName" to companyName,
                            "routeName" to it.routeLongName,
                            "routeId" to it.routeId
                        ),
                        StopsFragment.TAG
                    )
                    .addToBackStack(StopsFragment.TAG)
                    .commit()

            }
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        recyclerView.adapter = adapter

        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    private val routeQueryRunnable = Runnable {
        val dbName = "jetpacketa.db"
        val dbInputStream = requireActivity().applicationContext.assets.open(dbName)
        val helper = SqliteHelper.getInstance(dbInputStream, dbName)
        if(companyId == -1) return@Runnable

        val newRoutes = helper.getRoutes(companyId)

        requireActivity().runOnUiThread {
            routes.clear()
            routes.addAll(newRoutes)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onResume() {
        super.onResume()
        futureRunnable = executor.submit(routeQueryRunnable)
    }

    override fun onStop() {
        super.onStop()
        futureRunnable.cancel(true)
    }
}