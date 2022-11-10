package com.riis.jetpacketa.features.stop.room

import androidx.room.Dao
import androidx.room.Query

@Dao
interface StopDAO {
    @Query(
        "SELECT DISTINCT stops.* " +
                "  FROM trips " +
                "  INNER JOIN stop_times ON stop_times.trip_id = trips.trip_id and stop_times.agency_id = trips.agency_id " +
                "  INNER JOIN stops ON stops.stop_id = stop_times.stop_id and stops.agency_id = stop_times.agency_id" +
                "  WHERE trips.route_id = :routeId and trips.agency_id = :companyId;",
    )
    fun getStopsForRoute(routeId: Int, companyId: Int): List<Stop>
}