package com.riis.jetpacketa.features.stop

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.riis.jetpacketa.database.SqliteHelper
import com.riis.jetpacketa.features.stop.model.StopUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StopsViewModel(application: Application) : AndroidViewModel(application) {

    // Initialize Connection to DB
    private val dbName = "jetpacketa.db"
    private val dbInputStream = application.applicationContext.assets.open("jetpacketa.db")
    private val helper = SqliteHelper.getInstance(dbInputStream, dbName)

    // Create mutable live data for the `Company` list
    val stops = MutableLiveData<List<StopUi>>(emptyList())

    fun getStops(companyId: Int?, routeId: Int?) {
        if(companyId == null || companyId == -1) return
        if(routeId == null || routeId == -1) return

        // Launch new Coroutine for fetching the DB data
        viewModelScope.launch(Dispatchers.IO) {
            val newStops = helper.getStopsForRoute(routeId, companyId)

            // On the `Main` Thread, post the new companies
            withContext(Dispatchers.Main) {
                stops.postValue(newStops)
            }
        }

    }
}