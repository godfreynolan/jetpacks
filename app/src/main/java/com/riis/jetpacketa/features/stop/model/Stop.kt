package com.riis.jetpacketa.features.stop.model

data class Stop(
    val stopId: Int,
    val stopCode: Int,
    val stopName: String,
    val stopDesc: String,
    val stopLat: String,
    val stopLon: String,
    val zoneId: String?,
    val stopUrl: String?,
    val locationType: String?,
    val parentStation: String?,
    val stopTimezone: String?,
    val wheelChairBoarding: Int
)

data class StopUi(
    val stopId: Int,
    val stopName: String
)