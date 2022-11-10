package com.riis.jetpacketa.features.company.repository

import com.riis.jetpacketa.features.company.room.Company

interface CompanyRepository {
    fun getCompanies(): List<Company>
}