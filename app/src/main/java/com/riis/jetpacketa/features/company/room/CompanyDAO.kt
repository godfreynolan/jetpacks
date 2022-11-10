package com.riis.jetpacketa.features.company.room

import androidx.room.Dao
import androidx.room.Query

@Dao
interface CompanyDAO {
    @Query("SELECT * FROM agency")
    fun getCompanies(): List<Company>
}