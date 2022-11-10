package com.riis.jetpacketa.features.company.repository

import com.riis.jetpacketa.features.company.room.Company
import com.riis.jetpacketa.features.company.room.CompanyDAO
import javax.inject.Inject

class CompanyRepositoryImpl @Inject constructor(
    private val companyDAO: CompanyDAO
) : CompanyRepository {
    override fun getCompanies(): List<Company> {
        return companyDAO.getCompanies()
    }
}