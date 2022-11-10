package com.riis.jetpacketa.database

import com.riis.jetpacketa.features.company.model.Company
import com.riis.jetpacketa.features.route.model.Route
import com.riis.jetpacketa.features.stop.model.StopUi

interface SqliteHelperInterface {
    fun getCompanies(): List<Company>
    fun getRoutes(companyId: Int): List<Route>
    fun getStopsForRoute(routeId: Int, companyId: Int): List<StopUi>
}