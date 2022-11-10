package com.riis.jetpacketa.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.asFlow
import androidx.navigation.NavController
import com.riis.jetpacketa.features.route.RoutesViewModel
import com.riis.jetpacketa.features.route.room.Route
import com.riis.jetpacketa.ui.Screen
import com.riis.jetpacketa.ui.shared.ListViewItem

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun DefaultRoutesPreview() {
    val mockRouteItems = mutableListOf(
        Route(125,1,"125","Route 125","Sample Desc",1,null,null, null),
        Route(23,2,"23","Route 23","Sample Desc",1,null,null, null),
    )

    Surface {
        RoutesListView(routes = mockRouteItems) {}
    }
}

@Composable
fun RoutesScreenComposable(navController: NavController, companyId: Int, companyName: String) {
    val viewModel = hiltViewModel<RoutesViewModel>()
    viewModel.getRoutes(companyId)

    val titleText = if (companyName == "Detroit Department of Transportation") "DDOT" else companyName

    val routes: List<Route> by viewModel.routes.asFlow().collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "$titleText Routes") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, "Go back to companies fragment")
                    }
                },
            )
        }, content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
            ) {
                RoutesListView(routes = routes) { route ->
                    navController.navigate(
                        Screen.StopsScreen.withArgs(
                            companyId.toString(),
                            companyName,
                            route.routeId.toString(),
                            route.routeLongName ?: route.routeId.toString()))
                }
            }
        }
    )
}

@Composable
fun RoutesListView(routes: List<Route>, clicked: ((Route) -> (Unit))) {
    LazyColumn {
        items(routes) {
            ListViewItem(displayText = it.routeLongName ?: it.routeId.toString()) {
                clicked.invoke(it)
            }
        }
    }
}