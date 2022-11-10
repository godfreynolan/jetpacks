package com.riis.jetpacketa.features.route.model

data class Route(
    val routeId: Int,
    val companyId: Int,
    val routeShortName: Int,
    val routeLongName: String,
    val routeDesc: String,
    val routeType: Int,
    val routeUrl: String,
    val routeColor: String,
    val routeTextColor: String
)
