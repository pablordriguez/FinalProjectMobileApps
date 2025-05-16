package com.example.finamarvelapp.ui.screens

import android.app.Application
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.finamarvelapp.data.model.Hero
import com.example.finamarvelapp.viewmodel.FavoritesVM
import kotlinx.coroutines.launch

@Composable
fun HeroDetailDialog(
    hero: Hero,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val favoritesViewModel: FavoritesVM = viewModel(
        factory = object : ViewModelProvider.AndroidViewModelFactory(
            context.applicationContext as Application
        ) {}
    )

    val favorites by favoritesViewModel.favorites.collectAsState()
    var isFavorite by remember { mutableStateOf(false) }

    LaunchedEffect(favorites) {
        isFavorite = favorites.any { it.id == hero.id }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 8.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Text(
                    text = hero.name,
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(8.dp))

                val painter = rememberAsyncImagePainter(model = hero.images.lg)
                val state = painter.state

                Box(
                    modifier = Modifier
                        .size(width = 80.dp, height = 80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (state is AsyncImagePainter.State.Loading) {
                        CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.size(24.dp))
                    }

                    Image(
                        painter = painter,
                        contentDescription = hero.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                val fullName = hero.biography.fullName.ifBlank { "Not available." }
                val publisher = hero.biography.publisher.ifBlank { "Unknown publisher" }

                Text(
                    text = "Full Name: $fullName",
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = "Published by $publisher",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = {
                        scope.launch {
                            if (isFavorite) {
                                favoritesViewModel.removeFavorite(hero)
                            } else {
                                favoritesViewModel.addFavorite(hero)
                            }
                            isFavorite = !isFavorite
                        }
                    }) {
                        Text(if (isFavorite) "Remove from favorites" else "Add to favorites")
                    }

                    TextButton(onClick = {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "Hero: ${hero.name}\nFull name: ${hero.biography.fullName}\nImage: ${hero.images.lg}"
                            )
                        }
                        startActivity(context, Intent.createChooser(shareIntent, "Share with..."), null)
                    }) {
                        Text("Share")
                    }

                    TextButton(onClick = onDismiss) {
                        Text("Close")
                    }
                }
            }
        }
    }
}
