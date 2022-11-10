package com.riis.jetpacketa.features.stop

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.reflect.TypeToken
import com.riis.jetpacketa.database.SqliteHelper
import com.riis.jetpacketa.features.stop.model.StopUi
import com.riis.jetpacketa.security.EncryptedSharedPrefsHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StopsViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "StopViewModel"
        private const val FAVORITE_STOPS = "favorite_routes"
    }

    // Initialize Connection to DB
    private val dbName = "jetpacketa.db"
    private val dbInputStream = application.applicationContext.assets.open("jetpacketa.db")
    private val helper = SqliteHelper.getInstance(dbInputStream, dbName)
    private val encryptedSharedPrefs = EncryptedSharedPrefsHelper(application.applicationContext)

    // Create mutable live data for the `Company` list
    val stops = MutableLiveData<List<StopUi>>(emptyList())

    fun getStops(companyId: Int?, routeId: Int?) {
        if(companyId == null || companyId == -1) return
        if(routeId == null || routeId == -1) return

        // Launch new Coroutine for fetching the DB data
        viewModelScope.launch(Dispatchers.IO) {
            val newStops = helper.getStopsForRoute(routeId, companyId)
            val favorites = getFavorites()
            val formatted = newStops.map {
                if(favorites.find {fav -> it.stopId == fav.stopId && it.stopName == fav.stopName} != null) {
                    it.favorite = true
                }
                return@map it
            }

            // On the `Main` Thread, post the new companies
            withContext(Dispatchers.Main) {
                stops.postValue(formatted)
            }
        }

    }

    fun favorite(position: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val tempStops = stops.value?.toMutableList() ?: mutableListOf()
            tempStops[position].favorite = true
            val favorites = getFavorites()
            favorites.add(tempStops[position])
            encryptedSharedPrefs.savePreference(FAVORITE_STOPS, favorites)
            withContext(Dispatchers.Main) {
                stops.postValue(tempStops)
            }
        }
    }

    fun removeFavorite(stop: StopUi, position: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val tempStops = stops.value?.toMutableList() ?: mutableListOf()
            tempStops[position].favorite = false
            val favorites = getFavorites()
            favorites.removeIf { it.stopId == stop.stopId && it.stopName == stop.stopName }
            encryptedSharedPrefs.savePreference(FAVORITE_STOPS, favorites)

            withContext(Dispatchers.Main) {
                stops.postValue(tempStops)
            }
        }
    }

    private fun getFavorites(): MutableList<StopUi> {
        val typeToken = object : TypeToken<MutableList<StopUi>>() {}.type
        return encryptedSharedPrefs.getPreference(FAVORITE_STOPS, typeToken) ?: mutableListOf()
    }
}