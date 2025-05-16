package com.example.finamarvelapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.finamarvelapp.ui.navigation.NavigationGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val currentRoute = navController.currentBackStackEntryAsState()

            MaterialTheme {
                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            val route = currentRoute.value?.destination?.route

                            NavigationBarItem(
                                selected = route == "home",
                                onClick = { navController.navigate("home") },
                                icon = { Icon(Icons.AutoMirrored.Filled.List, null) },
                                label = { Text("Heroes") }
                            )

                            NavigationBarItem(
                                selected = route == "favorites",
                                onClick = { navController.navigate("favorites") },
                                icon = { Icon(Icons.Filled.Star, null) },
                                label = { Text("Favorites") }
                            )
                        }
                    }
                ) { paddingValues ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        NavigationGraph(navController = navController)
                    }
                }
            }
        }
    }
}
