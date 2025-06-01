package com.example.artspace.ui.screens

import androidx.compose.foundation.gestures.awaitHorizontalDragOrCancellation
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.artspace.repository.ArtworkRepository.artworks
import com.example.artspace.ui.components.ArtSpace

@Composable
fun ArtView() {
    val currentIndex = remember { mutableStateOf(0) }
    val (artwork, title, info) = artworks[currentIndex.value]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(700.dp)
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            val down = awaitPointerEvent().changes.firstOrNull() ?: continue
                            val drag = awaitHorizontalDragOrCancellation(down.id)
                            if (drag != null) {
                                val totalDrag = drag.position.x - down.position.x
                                if (totalDrag > 100 && currentIndex.value > 0) {
                                    currentIndex.value -= 1
                                } else if (totalDrag < -100 && currentIndex.value < artworks.lastIndex) {
                                    currentIndex.value += 1
                                }
                            }
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            ArtSpace(
                artwork = artwork,
                titleArt = title,
                infoArt = info
            )
        }

        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { if (currentIndex.value > 0) currentIndex.value -= 1 },
                enabled = currentIndex.value > 0
            ) { Text("Previous") }

            Button(
                onClick = { if (currentIndex.value < artworks.lastIndex) currentIndex.value += 1 },
                enabled = currentIndex.value < artworks.lastIndex
            ) { Text("Next") }
        }
    }
}
