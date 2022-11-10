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
import com.riis.jetpacketa.R
import com.riis.jetpacketa.database.SqliteHelper
import com.riis.jetpacketa.databinding.FragmentRouteBinding
import com.riis.jetpacketa.features.route.adapters.RouteRecyclerAdapter
import com.riis.jetpacketa.features.route.model.Route
import com.riis.jetpacketa.features.stop.StopsFragment
import java.util.concurrent.Executors
import java.util.concurrent.Future

class RoutesFragment: Fragment() {

    companion object {
        const val TAG = "RouteFragment"
    }

    private var _binding: FragmentRouteBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: RouteRecyclerAdapter
    private var executor = Executors.newSingleThreadExecutor()
    private lateinit var futureRunnable: Future<*>
    private val routes: MutableList<Route> = mutableListOf()
    private var companyId: Int = -1
    private var companyName: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRouteBinding.inflate(inflater, container, false)
        companyId = arguments?.getInt("companyId") ?: -1
        companyName = arguments?.getString("companyName", "") ?: ""

        if(companyName == "Detroit Department of Transportation") companyName = "DDOT"

        (activity as AppCompatActivity).supportActionBar?.title = "$companyName Routes"
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)


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
        binding.routeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.routeRecyclerView.adapter = adapter

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private val routeQueryRunnable = Runnable {
        val dbName = "gtfs_room.db"
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}