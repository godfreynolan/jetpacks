package com.riis.jetpacketa.features.stop.room

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "stops",
    primaryKeys = ["stop_id", "agency_id"]
)
data class Stop(
    @ColumnInfo(name = "stop_id")
    val stopId: Int,
    @ColumnInfo(name = "stop_code")
    val stopCode: String?,
    @ColumnInfo(name = "stop_name")
    val stopName: String?,
    @ColumnInfo(name = "stop_desc")
    val stopDesc: String?,
    @ColumnInfo(name = "stop_lat")
    val stopLat: String?,
    @ColumnInfo(name = "stop_lon")
    val stopLon: String?,
    @ColumnInfo(name = "zone_id")
    val zoneId: Int?,
    @ColumnInfo(name = "stop_url")
    val stopUrl: String?,
    @ColumnInfo(name = "location_type")
    val locationType: Int?,
    @ColumnInfo(name = "parent_station")
    val parentStation: Int?,
    @ColumnInfo(name = "stop_timezone")
    val stopTimezone: String?,
    @ColumnInfo(name = "wheelchair_boarding")
    val wheelChairBoarding: Int?,
    @ColumnInfo(name = "agency_id")
    val agencyId: Int
)

data class StopUi(
    val stopId: Int,
    val stopName: String,
    var favorite: Boolean = false
)