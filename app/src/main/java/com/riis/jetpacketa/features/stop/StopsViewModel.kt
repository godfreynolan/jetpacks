package com.riis.jetpacketa.features.stop

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riis.jetpacketa.features.stop.repository.StopRepository
import com.riis.jetpacketa.features.stop.room.StopUi
import com.google.gson.reflect.TypeToken
import com.riis.jetpacketa.security.EncryptedSharedPrefsHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

// Describes the ViewModel as a `HiltViewModel` and injects
// the `EncryptedSharedPrefsHelper` and `StopRepository`
@HiltViewModel
class StopsViewModel @Inject constructor(
    private val stopRepository: StopRepository,
    private val encryptedSharedPrefs: EncryptedSharedPrefsHelper
): ViewModel() {

    companion object {
        private const val TAG = "StopViewModel"
        private const val FAVORITE_STOPS = "favorite_routes"
    }

    // Create mutable live data for the `Company` list
    val stops = MutableLiveData<List<StopUi>>(emptyList())

    fun getStops(companyId: Int?, routeId: Int?) {
        if(companyId == null || companyId == -1) return
        if(routeId == null || routeId == -1) return

        // Launch new Coroutine for fetching the DB data
        viewModelScope.launch(Dispatchers.IO) {
            val newStops = stopRepository.getStops(routeId, companyId)
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