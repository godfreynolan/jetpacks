package com.riis.jetpacketa.features.route.room

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "routes", primaryKeys = ["route_id", "agency_id"])
data class Route(
    @ColumnInfo(name = "route_id")
    val routeId: Int,
    @ColumnInfo(name = "agency_id")
    val companyId: Int,
    @ColumnInfo(name = "route_short_name")
    val routeShortName: String?,
    @ColumnInfo(name = "route_long_name")
    val routeLongName: String?,
    @ColumnInfo(name = "route_desc")
    val routeDesc: String?,
    @ColumnInfo(name = "route_type")
    val routeType: Int,
    @ColumnInfo(name = "route_url")
    val routeUrl: String?,
    @ColumnInfo(name = "route_color")
    val routeColor: String?,
    @ColumnInfo(name = "route_text_color")
    val routeTextColor: String?
)
