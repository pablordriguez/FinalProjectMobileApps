package com.example.finamarvelapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.compose.AsyncImagePainter

@Composable
fun HeroImage(
    url: String,
    modifier: Modifier = Modifier.size(80.dp)
) {
    val imagePainter = rememberAsyncImagePainter(url)
    val painterState = imagePainter.state

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        if (painterState is AsyncImagePainter.State.Loading) {
            CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.size(24.dp))
        }

        Image(
            painter = imagePainter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}
