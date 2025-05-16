package com.example.finamarvelapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finamarvelapp.viewmodel.HeroViewModel
import com.example.finamarvelapp.data.model.Hero
import com.example.finamarvelapp.ui.components.HeroImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HeroListScreen(viewModel: HeroViewModel = viewModel()) {
    val isRefreshing = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val heroes = viewModel.heroList
    val loading = viewModel.isLoading

    var search by remember { mutableStateOf("") }
    var selectedHero by remember { mutableStateOf<Hero?>(null) }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing.value,
        onRefresh = {
            isRefreshing.value = true
            scope.launch {
                viewModel.reloadHeroes()
                isRefreshing.value = false
            }
        }
    )

    val filteredHeroes = heroes.filter {
        it.name.contains(search, ignoreCase = true)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        if (loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp)
            ) {
                item {
                    TextField(
                        value = search,
                        onValueChange = { search = it },
                        label = { Text("Search hero...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )
                }

                items(filteredHeroes) { hero ->
                    HeroCard(hero = hero) { selectedHero = hero }
                }

                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }
    }

    selectedHero?.let {
        HeroDetailDialog(hero = it, onDismiss = { selectedHero = null })
    }
}

@Composable
fun HeroCard(hero: Hero, onClick: () -> Unit) {
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
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(hero.name, style = MaterialTheme.typography.titleMedium)
                val fullName = hero.biography.fullName.ifBlank { "Unknown" }
                Text("Full Name: $fullName", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}
