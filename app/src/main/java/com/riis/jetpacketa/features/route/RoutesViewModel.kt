package com.riis.jetpacketa.features.route

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.riis.jetpacketa.database.SqliteHelper
import com.riis.jetpacketa.features.route.model.Route
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RoutesViewModel(application: Application) : AndroidViewModel(application) {

    // Initialize Connection to DB
    private val dbName = "jetpacketa.db"
    private val dbInputStream = application.applicationContext.assets.open("jetpacketa.db")
    private val helper = SqliteHelper.getInstance(dbInputStream, dbName)

    // Create mutable live data for the `Company` list
    val routes = MutableLiveData<List<Route>>(emptyList())

    fun getRoutes(companyId: Int?) {
        if(companyId == null || companyId == -1) return

        // Launch new Coroutine for fetching the DB data
        viewModelScope.launch(Dispatchers.IO) {
            val newRoutes = helper.getRoutes(companyId)

            // On the `Main` Thread, post the new companies
            withContext(Dispatchers.Main) {
                routes.postValue(newRoutes)
            }
        }

    }
}