package com.riis.jetpacketa.features.company.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "agency")
data class Company(
    @PrimaryKey
    @ColumnInfo(name = "agency_id")
    val id: Int,
    @ColumnInfo(name = "agency_name")
    val name: String,
    @ColumnInfo(name = "agency_url")
    val url: String,
    @ColumnInfo(name = "agency_timezone")
    val timezone: String,
    @ColumnInfo(name = "agency_lang")
    val lang: String?,
    @ColumnInfo(name = "agency_phone")
    val phone: String?,
    @ColumnInfo(name = "agency_fare_url")
    val fare_url: String? = null,
    @ColumnInfo(name = "agency_email")
    val email: String? = null
)
