package com.riis.jetpacketa.features.route.repository

import com.riis.jetpacketa.features.route.room.Route

interface RouteRepository {
    fun getRoutes(companyId: Int): List<Route>
}