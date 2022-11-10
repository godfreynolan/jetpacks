package com.riis.jetpacketa.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.riis.jetpacketa.features.stop.StopsViewModel
import com.riis.jetpacketa.features.stop.room.StopUi
import com.riis.jetpacketa.ui.shared.FavoriteStopListViewItem

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun DefaultStopsPreview() {
    val mockStopItems = mutableListOf(
        StopUi(123,"Meijer Taylor", false),
        StopUi(39,"W Jefferson + Cora", true),
    )
    Surface {
        StopsListView(stops = mockStopItems) { _, _ -> {} }
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
    Log.i("StopsScreen", "StopsScreenComposable: recomposing")
    val viewModel = hiltViewModel<StopsViewModel>()

    LaunchedEffect(key1 = Unit) {
        viewModel.getStops(companyId, routeId)
    }

    val stops = viewModel.stops.observeAsState(initial = emptyList())

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
                StopsListView(stops = stops.value) { stop, index ->
                    viewModel.invertFavorite(stop, index)
                }
            }
        }
    )
}

@Composable
fun StopsListView(stops: List<StopUi>, onClick: ((StopUi, Int) -> (Unit))) {
    LazyColumn {
        itemsIndexed(stops) { index, item ->
            FavoriteStopListViewItem(item) {
                onClick.invoke(item, index)
            }
        }
    }
}