package com.riis.jetpacketa.features.stop

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.riis.jetpacketa.R
import com.riis.jetpacketa.database.SqliteHelper
import com.riis.jetpacketa.features.stop.adapters.StopRecyclerAdapter
import com.riis.jetpacketa.features.stop.model.Stop
import com.riis.jetpacketa.features.stop.model.StopUi
import java.util.concurrent.Executors
import java.util.concurrent.Future

class StopsFragment: Fragment() {
    companion object {
        const val TAG = "StopsFragment"
    }

    private lateinit var adapter: StopRecyclerAdapter
    private lateinit var recyclerView: RecyclerView
    private var executor = Executors.newSingleThreadExecutor()
    private lateinit var futureRunnable: Future<*>
    private val stops: MutableList<StopUi> = mutableListOf()

    private var companyId: Int = -1
    private var companyName: String = ""
    private var routeName: String = ""
    private var routeId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_stops, container, false)
        companyId = arguments?.getInt("companyId") ?: -1
        routeId = arguments?.getInt("routeId") ?: -1
        companyName = arguments?.getString("companyName", "") ?: ""
        routeName = arguments?.getString("routeName", "") ?: ""

        (activity as AppCompatActivity).supportActionBar?.title = routeName
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Attach Adapter and Temporary Data to RecyclerView
        recyclerView = view.findViewById(R.id.stopsRecyclerView)

        adapter = StopRecyclerAdapter(stops).apply {
            onItemClicked = {
                // Item Clicked, start new fragment

            }
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        recyclerView.adapter = adapter

        return view
    }

    private val stopQueryRunnable = Runnable {
        val dbName = "gtfs_room.db"
        val dbInputStream = requireActivity().applicationContext.assets.open(dbName)
        val helper = SqliteHelper.getInstance(dbInputStream, dbName)
        val newStops = helper.getStopsForRoute(routeId, companyId)

        requireActivity().runOnUiThread {
            stops.clear()
            stops.addAll(newStops)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onResume() {
        super.onResume()
        futureRunnable = executor.submit(stopQueryRunnable)
    }

    override fun onStop() {
        super.onStop()
        futureRunnable.cancel(true)
    }

}