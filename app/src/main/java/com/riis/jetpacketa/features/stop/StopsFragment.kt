package com.riis.jetpacketa.features.stop

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.riis.jetpacketa.databinding.FragmentStopsBinding
import com.riis.jetpacketa.features.stop.adapters.StopRecyclerAdapter
import com.riis.jetpacketa.features.stop.room.StopUi
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StopsFragment: Fragment() {
    companion object {
        const val TAG = "StopsFragment"
    }

    private var _binding: FragmentStopsBinding? = null
    private val binding get() = _binding!!

    // Initialize the ViewModel
    private val viewModel by viewModels<StopsViewModel>()

    // Create an Observer that will automatically update
    // the recycler view when the data changes
    @SuppressLint("NotifyDataSetChanged")
    private val stopListObserver = Observer<List<StopUi>> {
        stops.clear()
        stops.addAll(it)
        adapter.notifyDataSetChanged()
    }

    private lateinit var adapter: StopRecyclerAdapter
    private val stops: MutableList<StopUi> = mutableListOf()

    private var companyId: Int = -1
    private var companyName: String = ""
    private var routeName: String = ""
    private var routeId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStopsBinding.inflate(inflater, container, false)

        // Get arguments from bundle
        companyId = arguments?.getInt("companyId") ?: -1
        routeId = arguments?.getInt("routeId") ?: -1
        companyName = arguments?.getString("companyName", "") ?: ""
        routeName = arguments?.getString("routeName", "") ?: ""

        viewModel.getStops(companyId, routeId)

        // Update Top Toolbar and display `Back` button
        (activity as AppCompatActivity).supportActionBar?.title = routeName
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        adapter = StopRecyclerAdapter(stops).apply {
            onItemClicked = { stop, isFavorite, position ->
                if(isFavorite) viewModel.favorite(position)
                else viewModel.removeFavorite(stop, position)
            }
        }

        // Setup recyclerView layout manager, decorations, and adapter
        binding.stopsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.stopsRecyclerView.adapter = adapter

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // When fragment resumes, start the observer
        viewModel.stops.observe(this, stopListObserver)
    }

    override fun onStop() {
        super.onStop()
        // When fragment stops, remove the observer
        viewModel.stops.removeObserver(stopListObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}