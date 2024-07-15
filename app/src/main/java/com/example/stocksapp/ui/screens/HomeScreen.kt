package com.example.stocksapp.ui.screens

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.stocksapp.data.dto.Stock
import com.example.stocksapp.data.local.StockCategory
import com.example.stocksapp.data.viewmodel.StockViewModel
import com.example.stocksapp.ui.component.StockItem
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.stocksapp.data.local.StockData
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    modifier: Modifier,
    stockViewModel: StockViewModel,
    snackbarHostState: SnackbarHostState?,
    onClick: (stock: Stock, stockData: StockData) -> Unit
) {

    var tabSelected by remember {
        mutableIntStateOf(0)
    }

    val isLoading by stockViewModel.isLoading.collectAsState(true)

    val topStock by stockViewModel.topStocks.collectAsState(emptyList())

    var isRefreshing by remember {
        mutableStateOf(false)
    }

    val tabs= listOf("TOP GAINERS","TOP LOSERS")

    var stockForGainers: LazyPagingItems<StockData>?

    var stockForLosers: LazyPagingItems<StockData>?

    Column(modifier = modifier.fillMaxSize(),  horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        if (isLoading) {
            Box (modifier = Modifier.fillMaxSize()){
                CircularProgressIndicator(
                    trackColor = Color.Green,
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.Center)
                )
            }
        }
        else if (topStock.isNotEmpty()){

            stockForGainers = stockViewModel.stockForTopGainers.collectAsLazyPagingItems()

            stockForLosers = stockViewModel.stockForTopLosers.collectAsLazyPagingItems()

            val loadStateOfGainers= stockForGainers?.loadState?.mediator

            val loadStateOfLosers= stockForLosers?.loadState?.mediator

            SwipeRefresh(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
                onRefresh = {
                    isRefreshing = true
                    stockViewModel.loadTopStock()
                    isRefreshing = false
                }
            ) {

                when (tabSelected) {
                    (0) -> {
                        Log.e("Assignment","Gainers Tab")
                        if ((loadStateOfGainers?.refresh is LoadState.Error) || (loadStateOfGainers?.append is LoadState.Error)) {
                            val isPaginatingError =
                                (loadStateOfGainers.append is LoadState.Error) || (stockForGainers?.itemCount ?: 0) > 1
                            val error = if (loadStateOfGainers.append is LoadState.Error)
                                (loadStateOfGainers.append as LoadState.Error).error
                            else
                                (loadStateOfGainers.refresh as LoadState.Error).error

                            val scope = rememberCoroutineScope()
                            LaunchedEffect(loadStateOfGainers) {
                                scope.launch {
                                    val result = snackbarHostState
                                        ?.showSnackbar(
                                            message = error.message.toString(),
                                            actionLabel = "Dismiss",
                                            duration = SnackbarDuration.Long
                                        )
                                    when (result) {
                                        SnackbarResult.ActionPerformed -> {

                                        }

                                        SnackbarResult.Dismissed -> {

                                        }

                                        null -> {}
                                    }
                                }
                            }
                        }

                        if (loadStateOfGainers?.refresh == LoadState.Loading) {
                            Log.e("Assignment","Refrershin")
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
                                if (stockForGainers != null) {
                                    ListGrid(
                                        modifier = Modifier.fillMaxWidth(), stockForGainers!!,
                                        topStock.filter { it.stockCategory == StockCategory.TOP_GAINERS.stockCategory }
                                            .flatMap { it.stocks },
                                        onClick
                                    )
                                }
                            if(loadStateOfGainers?.append == LoadState.Loading){
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
                            }
                        }
                    }

                    (1) -> {
                        Log.e("Assignment","Losers Tab")
                        if ((loadStateOfLosers?.refresh is LoadState.Error) || (loadStateOfLosers?.append is LoadState.Error)) {
                            val isPaginatingError =
                                (loadStateOfLosers.append is LoadState.Error) || (stockForLosers?.itemCount ?: 0) > 1
                            val error = if (loadStateOfLosers.append is LoadState.Error)
                                (loadStateOfLosers.append as LoadState.Error).error
                            else {
                                (loadStateOfLosers.refresh as LoadState.Error).error
                            }
                            val scope = rememberCoroutineScope()

                            LaunchedEffect(loadStateOfLosers) {
                                scope.launch {
                                    val result = snackbarHostState
                                        ?.showSnackbar(
                                            message = error.message.toString(),
                                            actionLabel = "Dismiss",
                                            duration = SnackbarDuration.Long
                                        )
                                    when (result) {
                                        SnackbarResult.ActionPerformed -> {

                                        }

                                        SnackbarResult.Dismissed -> {

                                        }

                                        null -> {}
                                    }
                                }
                            }
                        }


                        if (loadStateOfLosers?.append == LoadState.Loading) {
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
                            if (stockForLosers != null) {
                                ListGrid(
                                    modifier = Modifier.fillMaxWidth(), stockForLosers!!,
                                    topStock.filter { it.stockCategory == StockCategory.TOP_LOSERS.stockCategory }
                                        .flatMap { it.stocks },
                                    onClick
                                )
                            }
                            if(loadStateOfLosers?.append == LoadState.Loading){
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
                            }
                        }
                    }
                }
            }
        }
        else{
            Text(
                text = "Top Stock Data is Not Available",
                modifier = Modifier.fillMaxSize(),
                fontSize = 20.sp
            )
        }

        TabRow(
            selectedTabIndex = tabSelected, modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)
        ) {
            tabs.forEachIndexed { index, s ->
                Tab(selected = tabSelected == index,
                    modifier = Modifier.border(
                        0.dp, Color.Black,
                        RectangleShape
                    ),
                    selectedContentColor = Color.Green.copy(0.9f),
                    unselectedContentColor = Color.Yellow,
                    onClick = {
                        Log.e("Assignment",index.toString())
                        tabSelected = index },
                    text = {
                        Text(text = s, color = Color.Green.copy(0.9f))
                    })
            }
        }
    }

}

@Composable
fun ListGrid(modifier: Modifier,listOfStockInfo: LazyPagingItems<StockData>, list:List<Stock>, onClick: (stock:Stock,stockData: StockData) -> Unit){
    LazyVerticalGrid(
        modifier = modifier
            .fillMaxHeight(0.93f)
            .fillMaxWidth(),
        columns = GridCells.Adaptive(minSize = (LocalConfiguration.current.screenWidthDp*0.40).dp)
    ) {
        items(listOfStockInfo.itemCount) { idx ->
            val stock= listOfStockInfo[idx]
            StockItem(
                modifier = Modifier
                    .padding(13.dp)
                    .wrapContentSize()
                    .clickable { stock?.let { onClick(list[idx], it) } },list[idx]
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun HomePreview() {
//
//    val viewModel = hiltViewModel<StockViewModel>()
//
//    StocksAppTheme {
//        HomeScreen(Modifier.padding(0.dp),viewModel,null,{ a,b -> })
//    }
//
//}