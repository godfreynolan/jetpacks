package com.riis.jetpacketa.features.stop.repository

import com.riis.jetpacketa.features.stop.room.StopDAO
import com.riis.jetpacketa.features.stop.room.StopUi
import javax.inject.Inject

class StopRepositoryImpl @Inject constructor(
    private val stopDAO: StopDAO
): StopRepository {

    override fun getStops(routeId: Int, companyId: Int): List<StopUi> {
        val stops = stopDAO.getStopsForRoute(routeId, companyId)
        return stops.map { StopUi(it.stopId, it.stopName ?: it.stopId.toString()) }
    }
}