package com.riis.jetpacketa.ui.shared

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.riis.jetpacketa.ui.theme.Shapes
import com.riis.jetpacketa.ui.theme.Typography

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun DefaultListViewPreview() {
    ListViewItem(displayText = "SMART")
}

@Composable
fun ListViewItem(displayText: String, onClick: (() -> Unit)? = null) {
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
            Text (
                text = displayText,
                style = Typography.body1,
                modifier = Modifier
                    .widthIn(18.dp)
                    .padding(
                        start = 10.dp,
                        end = 10.dp,
                        top = 5.dp,
                        bottom = 5.dp
                    )
            )
        }
    }
}