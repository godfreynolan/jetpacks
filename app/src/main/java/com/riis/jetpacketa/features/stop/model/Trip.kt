package com.riis.jetpacketa.features.stop.model

data class Trip(
    val tripId: Int,
    val routeId: Int,
    val serviceId: Int,
    val tripHeadSign: String,
    val tripShortName: String?,
    val tripDirectionId: Int,
    val blockId: Int,
    val shapeId: String,
    val wheelchairAccessible: Int,
    val bikesAllowed: Int
)
