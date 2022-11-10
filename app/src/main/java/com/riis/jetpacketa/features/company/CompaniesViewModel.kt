package com.riis.jetpacketa.features.company

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riis.jetpacketa.database.SqliteHelperInterface
import com.riis.jetpacketa.features.company.model.Company
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

// Describes the ViewModel as a `HiltViewModel` and injects
// the `SqliteHelper`
@HiltViewModel
class CompaniesViewModel @Inject constructor(
    private val helper: SqliteHelperInterface
): ViewModel() {
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