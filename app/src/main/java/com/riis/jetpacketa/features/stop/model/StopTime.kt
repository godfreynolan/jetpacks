package com.riis.jetpacketa.features.stop.model

data class StopTime(
    val tripId: Int,
    val arrivalTime: String,
    val departureTime: String,
    val stopId: Int,
    val stopSequence: Int,
    val stopHeadSign: String,
    val pickUpType: Int,
    val dropOffType: Int,
    val shapeDistTraveled: String,
    val timePoint: Int
)
