package com.example.stocksapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.stocksapp.ui.screens.HomeScreen
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.stocksapp.data.dto.Stock
import com.example.stocksapp.data.viewmodel.StockViewModel
import com.example.stocksapp.ui.component.TopBar
import com.example.stocksapp.ui.screens.DetailScreen

@Composable
fun Navigation(stockViewModel: StockViewModel) {
    val navController = rememberNavController()

    val route= navController.currentBackStackEntryAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(modifier =Modifier.fillMaxWidth() , route = route.value?.destination?.route?:"home_screen")
        NavHost(navController = navController, startDestination = Screen.HomeScreen.route)
        {

            composable(Screen.HomeScreen.route) {
                Log.e("Home Screen", "home")
                HomeScreen(
                    Modifier,
                    stockViewModel
                ) {
                    stockViewModel.loadInfraData("IBM")
                    stockViewModel.loadStockInfo(it)
                    navController.navigate(Screen.DetailScreen.route)
                }
            }

            composable(Screen.DetailScreen.route) {
                Log.e("Detail Screen", "detail")
                DetailScreen(Modifier,stockViewModel, Stock())
            }

        }
    }

    val lifecycleState = LocalLifecycleOwner.current.lifecycle.currentStateFlow.collectAsState()

    val interCount by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(lifecycleState.value) {
        Log.e("Grow App",lifecycleState.value.toString()+" "+ interCount)
        if(lifecycleState.value == Lifecycle.State.STARTED||interCount==0){
            Log.e("Grow App",lifecycleState.value.toString())
            stockViewModel.loadTopStocks()
        }
        interCount.inc()
    }


}