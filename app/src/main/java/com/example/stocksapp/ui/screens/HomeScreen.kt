package com.example.stocksapp.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stocksapp.ui.component.StockItem
import com.example.stocksapp.ui.theme.StocksAppTheme

@Composable
fun HomeScreen(){

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp)
    ) {
        items(10) { _ ->
            StockItem(modifier = Modifier.width(120.dp).padding(10.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    StocksAppTheme {
        HomeScreen()
    }
}