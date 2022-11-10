package com.riis.jetpacketa.features.route.repository

import com.riis.jetpacketa.features.route.room.Route
import com.riis.jetpacketa.features.route.room.RouteDAO
import javax.inject.Inject

class RouteRepositoryImpl @Inject constructor(
    private val routeDAO: RouteDAO
) : RouteRepository {

    override fun getRoutes(companyId: Int): List<Route> {
        return routeDAO.getRoutes(companyId)
    }
}