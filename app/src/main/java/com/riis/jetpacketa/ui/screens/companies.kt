package com.riis.jetpacketa.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.asFlow
import androidx.navigation.NavController
import com.riis.jetpacketa.features.company.CompaniesViewModel
import com.riis.jetpacketa.features.company.room.Company
import com.riis.jetpacketa.ui.Screen
import com.riis.jetpacketa.ui.shared.ListViewItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun DefaultCompaniesPreview() {
    val mockCompanyItems = mutableListOf<Company>(
        Company(1,"SMART","example.com","America/Detroit","en",null,null,null),
        Company(2,"DDOT","example.com","America/Detroit","en",null,null,null)
    )
    Surface {
        CompaniesListView(companies = mockCompanyItems) {}
    }
}

@Composable
fun CompaniesScreenComposable(navController: NavController) {
    val viewModel = hiltViewModel<CompaniesViewModel>()
    viewModel.getCompanies()

    val companies: List<Company> by viewModel.companies.asFlow().collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Companies") },
            )
        }, content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
            ) {
                CompaniesListView(companies = companies) { company ->
                    navController.navigate(Screen.RoutesScreen.withArgs(company.id.toString(), company.name))
                }
            }
        }
    )
}

@Composable
fun CompaniesListView(companies: List<Company>, clicked: ((Company) -> (Unit))) {
    LazyColumn {
        items(companies) {
            ListViewItem(displayText = it.name) { clicked.invoke(it) }
        }
    }
}