package com.riis.jetpacketa.features.company

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.riis.jetpacketa.database.SqliteHelper
import com.riis.jetpacketa.features.company.model.Company
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CompaniesViewModel(application: Application) : AndroidViewModel(application) {

    // Initialize Connection to DB
    private val dbName = "jetpacketa.db"
    private val dbInputStream = application.applicationContext.assets.open("jetpacketa.db")
    private val helper = SqliteHelper.getInstance(dbInputStream, dbName)

    // Create mutable live data for the `Company` list
    val companies = MutableLiveData<List<Company>>(emptyList())

    fun getCompanies() {
        // Launch new Coroutine for fetching the DB data
        viewModelScope.launch(Dispatchers.IO) {
            val newCompanies = helper.getCompanies()

            // On the `Main` Thread, post the new companies
            withContext(Dispatchers.Main) {
                companies.postValue(newCompanies)
            }
        }

    }
}