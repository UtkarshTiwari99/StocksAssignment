package com.example.stocksapp.ui.navigation

sealed class Screen (val route:String){
    data object HomeScreen: Screen("home_screen")
    data object SplashScreen: Screen("splash_screen")
    data object DetailScreen :Screen("detail_screen")
    data object SearchScreen :Screen("search_screen")
}