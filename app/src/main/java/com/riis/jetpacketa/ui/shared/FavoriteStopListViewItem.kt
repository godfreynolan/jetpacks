package com.riis.jetpacketa.ui.shared

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.riis.jetpacketa.ui.theme.Shapes
import com.riis.jetpacketa.ui.theme.Typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.ui.Alignment
import com.riis.jetpacketa.features.stop.room.StopUi

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun DefaultFavoriteListViewPreview() {
    FavoriteStopListViewItem(stop = StopUi(32, "Test & Sample", false))
}

@Composable
fun FavoriteStopListViewItem(stop: StopUi, onClick: (() -> Unit)? = null) {
    Row {
        Card(
            elevation = 5.dp,
            shape = Shapes.medium,
            modifier = Modifier
                .padding(
                    start = 10.dp,
                    end = 10.dp,
                    top = 5.dp,
                    bottom = 5.dp
                )
                .fillMaxWidth()
                .clickable { onClick?.invoke() }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text (
                    text = stop.stopName,
                    style = Typography.body1,
                    modifier = Modifier
                        .padding(
                            start = 10.dp,
                            end = 10.dp,
                            top = 5.dp,
                            bottom = 5.dp
                        )
                )
                IconButton(
                    onClick = { /*TODO*/ }
                ) {
                    Icon(
                        if(stop.favorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        "Favorite this item.",
                        tint = MaterialTheme.colors.primary,
                    )
                }
            }
        }
    }
}