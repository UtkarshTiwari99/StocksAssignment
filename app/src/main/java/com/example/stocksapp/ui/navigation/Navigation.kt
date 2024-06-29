package com.example.stocksapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.stocksapp.ui.screens.HomeScreen
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.HomeScreen.route){
        composable(Screen.HomeScreen.route) {
            Log.e("Home Screen", "home")
            HomeScreen(Modifier.padding(0.dp))
        }
        composable(Screen.SplashScreen.route) {
            Log.e("Splash Screen", "splash")
            HomeScreen(Modifier.padding(0.dp))
        }
    }
}