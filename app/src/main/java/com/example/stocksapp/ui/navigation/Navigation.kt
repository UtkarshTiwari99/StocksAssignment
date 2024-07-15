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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.stocksapp.data.viewmodel.StockViewModel
import com.example.stocksapp.ui.component.TopBar
import com.example.stocksapp.ui.screens.DetailScreen

@Composable
fun Navigation(snackbarHostState: SnackbarHostState) {

    val navController = rememberNavController()

    val stockViewModel =
        hiltViewModel<StockViewModel>()

    val currentStock by stockViewModel.currentStock.collectAsState(null)

    val route= navController.currentBackStackEntryAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(modifier =Modifier.fillMaxWidth() , route = route.value?.destination?.route?:"home_screen")
        NavHost(navController = navController, startDestination = Screen.HomeScreen.route)
        {

            composable(Screen.HomeScreen.route) {
                Log.e("Home Screen", "home")
                HomeScreen(
                    Modifier,
                    stockViewModel,snackbarHostState
                ) {stock,stockData ->
                    stockViewModel.loadStockDetail(stockData,stock)
                    navController.navigate(Screen.DetailScreen.route)
                }
            }

            composable(Screen.DetailScreen.route) {
                Log.e("Detail Screen", "detail")
                currentStock?.let { it1 ->
                    DetailScreen(Modifier,stockViewModel, it1) }
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
            Log.e("paurush","refreshed")
        }
        interCount.inc()
    }


}