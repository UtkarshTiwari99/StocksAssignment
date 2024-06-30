package com.example.stocksapp.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.stocksapp.data.dto.Stock
import com.example.stocksapp.data.viewmodel.StockViewModel
import com.example.stocksapp.ui.component.StockItem

@Composable
fun HomeScreen(modifier: Modifier,stockViewModel: StockViewModel,onClick:(stock:Stock)->Unit,) {

    var tabSelected by remember {
        mutableIntStateOf(0)
    }

    val topGainer by stockViewModel.topGainers.collectAsState(emptyList())

    val topLoser by stockViewModel.topLosers.collectAsState(emptyList())

    val tabs= listOf("TOP GAINERS","TOP LOSERS")

    Column(modifier = modifier.fillMaxSize()) {
        when(tabSelected){
            (0)-> {
                val isLoading by stockViewModel.isLoading.collectAsState(true)
                if (isLoading) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            trackColor = Color.Green,
                            modifier = Modifier.size(50.dp)
                        )
                    }
                } else {
                    ListGrid(modifier = Modifier.fillMaxWidth(), topGainer, onClick)
                }}
            (1)-> {
                val isLoading by stockViewModel.isLoading.collectAsState(true)
                if (isLoading) {
                    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        CircularProgressIndicator(
                            trackColor = Color.Green,
                            modifier = Modifier.size(50.dp)
                        )
                    }
                } else {
                ListGrid(modifier = Modifier.fillMaxWidth(), topLoser, onClick)}}
        }
        TabRow(
//            divider = {
//            Column {
//                VerticalDivider(
//                    modifier = Modifier.align(Alignment.CenterHorizontally),
//                    thickness = 2.1.dp, color = Color.Black
//                )
//            }
//        },
            selectedTabIndex = tabSelected
        ) {
            tabs.forEachIndexed { index, s ->
                Tab(selected = tabSelected == index, modifier = Modifier.border(
                    1.dp, Color.Black,
                    RectangleShape
                ), selectedContentColor = Color.Green.copy(0.9f)
                        , unselectedContentColor = Color.Black
                    ,onClick = { tabSelected = index }, text = {
                    Text(text = s)
                })
            }
        }
    }

}

@Composable
fun ListGrid(modifier: Modifier, list:List<Stock>, onClick: (stock: Stock) -> Unit){
    LazyVerticalGrid(
        modifier = modifier
            .fillMaxHeight(0.93f)
            .fillMaxWidth(),
        columns = GridCells.Adaptive(minSize = (LocalConfiguration.current.screenWidthDp*0.40).dp)
    ) {
        items(list.size) { idx ->
            StockItem(
                modifier = Modifier
                    .padding(13.dp)
                    .wrapContentSize()
                    .clickable { onClick(list[idx]) },list[idx]
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun HomePreview() {
//    StocksAppTheme {
//        HomeScreen(Modifier.padding(0.dp), stockData = TopStockData()){}
//    }
//}