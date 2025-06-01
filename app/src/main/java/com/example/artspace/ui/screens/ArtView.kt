package com.example.artspace.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitHorizontalDragOrCancellation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.artspace.repository.ArtworkRepository.artworks
import com.example.artspace.ui.components.ArtSpace
import com.example.artspace.ui.theme.lightBlue
import com.example.artspace.ui.theme.lightBlueDisabled
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.animateColorAsState
import androidx.compose.ui.draw.clip

@Preview(showBackground = true)
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
            Crossfade(targetState = currentIndex.value, label = "ArtChange") { index ->
                val (artwork, title, info) = artworks[index]
                ArtSpace(
                    artwork = artwork,
                    titleArt = title,
                    infoArt = info
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            artworks.forEachIndexed { index, _ ->
                val isSelected = index == currentIndex.value

                val dotSize by animateDpAsState(
                    targetValue = if (isSelected) 12.dp else 8.dp,
                    label = "dot size"
                )
                val dotColor by animateColorAsState(
                    targetValue = if (isSelected) lightBlue else lightBlueDisabled,
                    label = "dot color"
                )

                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(dotSize)
                        .clip(CircleShape)
                        .background(dotColor)
                )
            }
        }

        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { if (currentIndex.value > 0) currentIndex.value -= 1 },
                enabled = currentIndex.value > 0,
                colors = ButtonDefaults.buttonColors(containerColor = lightBlue)
            ) { Text("Previous", color = Color.White) }

            Button(
                onClick = { if (currentIndex.value < artworks.lastIndex) currentIndex.value += 1 },
                enabled = currentIndex.value < artworks.lastIndex,
                colors = ButtonDefaults.buttonColors(containerColor = lightBlue)
            ) { Text("Next", color = Color.White) }
        }
    }
}
