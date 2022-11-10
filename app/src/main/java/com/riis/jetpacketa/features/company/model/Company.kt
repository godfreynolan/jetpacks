package com.riis.jetpacketa.features.company.model

data class Company(
    val id: Int,
    val name: String,
    val url: String,
    val timezone: String,
    val lang: String,
    val phone: String,
    val fare_url: String? = null,
    val email: String? = null
)
