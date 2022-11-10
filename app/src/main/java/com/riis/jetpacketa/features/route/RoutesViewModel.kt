package com.riis.jetpacketa.features.route

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riis.jetpacketa.features.route.repository.RouteRepository
import com.riis.jetpacketa.features.route.room.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

// Describes the ViewModel as a `HiltViewModel` and injects
// the `SqliteHelper`
@HiltViewModel
class RoutesViewModel @Inject constructor(
    private val routeRepository: RouteRepository
): ViewModel() {

    // Create mutable live data for the `Company` list
    val routes = MutableLiveData<List<Route>>(emptyList())

    fun getRoutes(companyId: Int?) {
        if(companyId == null || companyId == -1) return

        // Launch new Coroutine for fetching the DB data
        viewModelScope.launch(Dispatchers.IO) {
            val newRoutes = routeRepository.getRoutes(companyId)

            // On the `Main` Thread, post the new companies
            withContext(Dispatchers.Main) {
                routes.postValue(newRoutes)
            }
        }

    }
}