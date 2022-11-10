package com.riis.jetpacketa.features.route

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
import com.riis.jetpacketa.databinding.FragmentRouteBinding
import com.riis.jetpacketa.features.route.adapters.RouteRecyclerAdapter
import com.riis.jetpacketa.features.route.model.Route
import com.riis.jetpacketa.features.stop.StopsFragment

class RoutesFragment: Fragment() {

    companion object {
        const val TAG = "RouteFragment"
    }

    private var _binding: FragmentRouteBinding? = null
    private val binding get() = _binding!!

    // Initialize the ViewModel
    private val viewModel by viewModels<RoutesViewModel>()

    // Create an Observer that will automatically update
    // the recycler view when the data changes
    @SuppressLint("NotifyDataSetChanged")
    private val routeListObserver = Observer<List<Route>> {
        routes.clear()
        routes.addAll(it)
        adapter.notifyDataSetChanged()
    }

    private lateinit var adapter: RouteRecyclerAdapter
    private val routes: MutableList<Route> = mutableListOf()
    private var companyId: Int = -1
    private var companyName: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRouteBinding.inflate(inflater, container, false)

        // Get the arguments from the Bundle
        companyId = arguments?.getInt("companyId") ?: -1
        companyName = arguments?.getString("companyName", "") ?: ""

        // Replace the long name of DDOT with the acronym
        if(companyName == "Detroit Department of Transportation") companyName = "DDOT"

        // Fetch the routes
        viewModel.getRoutes(companyId)

        // Update Top Toolbar with new text and enable the `Back` button
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

        // Setup recyclerView's layout manager, decorations, and adapter
        binding.routeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.routeRecyclerView.adapter = adapter

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // When fragment resumes, start observer
        viewModel.routes.observe(this, routeListObserver)
    }

    override fun onStop() {
        super.onStop()
        // When fragment stops, remove the live data observer
        viewModel.routes.removeObserver(routeListObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}