package com.riis.jetpacketa.features.stop.repository

import com.riis.jetpacketa.features.stop.room.StopUi

interface StopRepository {
    fun getStops(routeId: Int, companyId: Int): List<StopUi>
}