package com.example.stocksapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stocksapp.ui.component.StockItem
import com.example.stocksapp.ui.theme.StocksAppTheme

@Composable
fun HomeScreen(padding: Modifier) {

    var tabSelected by remember {
        mutableIntStateOf(0)
    };
    val tabs= listOf("TOP GAINERS","TOP LOSERS");

    Column(modifier = padding.fillMaxSize()) {
        when(tabSelected){
            (0)-> ListGrid(modifier = Modifier.fillMaxWidth(), emptyList())
            (1)-> ListGrid(modifier = Modifier.fillMaxWidth(), emptyList())
        }
        TabRow(divider = {
            Column {
                VerticalDivider(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    thickness = 2.1.dp, color = Color.Black
                )
            }
        }, selectedTabIndex = tabSelected) {
            tabs.forEachIndexed { index, s ->
                Tab(selected = tabSelected == index, onClick = { tabSelected = index }, text = {
                    Text(text = s)
                })
            }
        }
    }

}

@Composable
fun ListGrid(modifier: Modifier,list:List<String>){
    LazyVerticalGrid(
        modifier = modifier
            .fillMaxHeight(0.93f)
            .fillMaxWidth(),
        columns = GridCells.Adaptive(minSize = (LocalConfiguration.current.screenWidthDp*0.40).dp)
    ) {
        items(10) { _ ->
            StockItem(
                modifier = Modifier
                    .padding(13.dp)
                    .wrapContentSize()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    StocksAppTheme {
        HomeScreen(Modifier.padding(0.dp))
    }
}