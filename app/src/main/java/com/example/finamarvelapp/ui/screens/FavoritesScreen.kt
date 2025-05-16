package com.example.finamarvelapp.ui.screens

import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finamarvelapp.data.model.Hero
import com.example.finamarvelapp.viewmodel.FavoritesVM
import com.example.finamarvelapp.ui.components.HeroImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen() {
    val context = LocalContext.current
    val favoritesVM: FavoritesVM = viewModel(
        factory = object : ViewModelProvider.AndroidViewModelFactory(
            context.applicationContext as Application
        ) {}
    )
    val favorites by favoritesVM.favorites.collectAsState()
    var query by remember { mutableStateOf("") }
    var selected by remember { mutableStateOf<Hero?>(null) }

    val filtered = favorites.filter {
        it.name.contains(query, ignoreCase = true)
    }.sortedBy { it.name }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("My Favorites") })
        }
    ) { padding ->
        if (favorites.isEmpty()) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No favorites found.")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                TextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("Search favorite...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                LazyColumn {
                    items(filtered) { hero ->
                        FavoriteCard(hero) { selected = hero }
                    }
                }
            }
        }

        selected?.let {
            HeroDetailDialog(hero = it, onDismiss = { selected = null })
        }
    }
}

@Composable
fun FavoriteCard(hero: Hero, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            HeroImage(url = hero.images.sm)
            Spacer(Modifier.width(12.dp))
            Column {
                Text(hero.name, style = MaterialTheme.typography.titleMedium)
                val name = hero.biography.fullName.ifBlank { "Unknown" }
                Text("Full Name: $name", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}
