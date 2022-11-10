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
import com.riis.jetpacketa.features.stop.StopsViewModel
import com.riis.jetpacketa.features.stop.room.StopUi
import com.riis.jetpacketa.ui.Screen
import com.riis.jetpacketa.ui.shared.ListViewItem


@Composable
@Preview(showBackground = true, showSystemUi = true)
fun DefaultStopsPreview() {
    val mockStopItems = mutableListOf(
        StopUi(123,"Meijer Taylor"),
        StopUi(39,"W Jefferson + Cora"),
    )
    Surface {
        StopsListView(stops = mockStopItems)
    }
}

@Composable
fun StopsScreenComposable(
    navController: NavController,
    companyId: Int,
    companyName: String,
    routeId: Int,
    routeName: String
) {
    val viewModel = hiltViewModel<StopsViewModel>()
    viewModel.getStops(companyId, routeId)

    val stops: List<StopUi> by viewModel.stops.asFlow().collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "$routeName Stops") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, "Go back to routes fragment")
                    }
                },
            )
        }, content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
            ) {
                StopsListView(stops = stops)
            }
        }
    )
}

@Composable
fun StopsListView(stops: List<StopUi>) {
    LazyColumn {
        items(stops) {
            ListViewItem(displayText = it.stopName) {}
        }
    }
}