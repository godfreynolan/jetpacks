package com.riis.jetpacketa.features.route.room

import androidx.room.Dao
import androidx.room.Query

@Dao
interface RouteDAO {
    @Query("SELECT * FROM routes WHERE agency_id = :companyId")
    fun getRoutes(companyId: Int): List<Route>
}