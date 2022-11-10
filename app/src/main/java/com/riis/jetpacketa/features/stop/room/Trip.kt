package com.riis.jetpacketa.features.stop.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "trips",
    primaryKeys = ["trip_id", "route_id", "direction_id", "shape_id", "agency_id"],
    indices = [
        Index(value = arrayOf("route_id"))
    ]
)
data class Trip(
    @ColumnInfo(name = "trip_id")
    val tripId: Int,
    @ColumnInfo(name = "route_id")
    val routeId: Int,
    @ColumnInfo(name = "service_id")
    val serviceId: Int,
    @ColumnInfo(name = "trip_headsign")
    val tripHeadSign: String?,
    @ColumnInfo(name = "trip_short_name")
    val tripShortName: String?,
    @ColumnInfo(name = "direction_id")
    val directionId: Int,
    @ColumnInfo(name = "block_id")
    val blockId: Int?,
    @ColumnInfo(name = "shape_id")
    val shapeId: String,
    @ColumnInfo(name = "wheelchair_accessible")
    val wheelchairAccessible: Int?,
    @ColumnInfo(name = "bikes_allowed")
    val bikesAllowed: Int?,
    @ColumnInfo(name = "agency_id")
    val agencyId: Int
)
