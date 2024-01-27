package com.example.wordlehelper.ui.view.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordlehelper.model.WordInfo

@Composable
fun TopGuessesGrid(
    topGuesses: List<WordInfo>,
    modifier: Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        modifier = modifier
    ) {
        itemsIndexed(topGuesses) { index, guess ->
            Card(
                backgroundColor = MaterialTheme.colors.surface,
                modifier = Modifier
                    .padding(8.dp)
                    .height(64.dp)
            ) {
                Column(
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text(
                        text = "${index + 1}. ${guess.word}",
                        style = MaterialTheme.typography.subtitle1.copy(fontSize = 20.sp),
                        color = MaterialTheme.colors.background
                    )
                    Text(
                        text = String.format("%.3f", guess.infoScore),
                        style = MaterialTheme.typography.caption.copy(fontSize = 18.sp),
                        color = MaterialTheme.colors.background
                    )
                }
            }
        }
    }
}