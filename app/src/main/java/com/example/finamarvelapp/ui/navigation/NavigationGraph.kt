package com.example.finamarvelapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.finamarvelapp.ui.screens.FavoritesScreen
import com.example.finamarvelapp.ui.screens.HeroListScreen

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HeroListScreen()
        }
        composable("favorites") {
            FavoritesScreen()
        }
    }
}
