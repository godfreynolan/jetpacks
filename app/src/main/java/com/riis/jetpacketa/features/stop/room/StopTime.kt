package com.riis.jetpacketa.features.stop.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "stop_times",
    primaryKeys = ["trip_id", "stop_id", "stop_sequence", "agency_id"],
    indices = [
        Index(value = arrayOf("trip_id"))
    ]
)
data class StopTime(
    @ColumnInfo(name = "trip_id")
    val tripId: Int,
    @ColumnInfo(name = "arrival_time")
    val arrivalTime: String?,
    @ColumnInfo(name = "departure_time")
    val departureTime: String?,
    @ColumnInfo(name = "stop_id")
    val stopId: Int,
    @ColumnInfo(name = "stop_sequence")
    val stopSequence: Int,
    @ColumnInfo(name = "stop_headsign")
    val stopHeadSign: String?,
    @ColumnInfo(name = "pickup_type")
    val pickUpType: Int?,
    @ColumnInfo(name = "drop_off_type")
    val dropOffType: Int?,
    @ColumnInfo(name = "shape_dist_traveled")
    val shapeDistTraveled: String?,
    @ColumnInfo(name = "timepoint")
    val timePoint: Int?,
    @ColumnInfo(name = "agency_id")
    val agencyId: Int
)
