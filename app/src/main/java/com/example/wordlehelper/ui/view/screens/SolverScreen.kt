package com.example.wordlehelper.ui.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.wordlehelper.ui.view.components.GameGrid
import com.example.wordlehelper.ui.view.components.TopGuessesGrid
import com.example.wordlehelper.ui.viewmodel.SolverViewModel

@Composable
fun SolverScreen(viewModel: SolverViewModel) {
    val topGuesses by viewModel.topGuesses.collectAsState()
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {
        TopGuessesGrid(
            topGuesses = topGuesses,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        GameGrid(context, viewModel)
        ControlButtons(viewModel)
    }
}

@Composable
fun ControlButtons(viewModel: SolverViewModel) {
    Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = { viewModel.onCalculatePressed() },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Calculate")
        }

        Button(
            onClick = { viewModel.clearBoard() },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Clear Board")
        }
    }
}

fun isValidWord(word: String): Boolean {
    //TODO: fix letter validation
    return word.length == 5 && word.all { it.isLetter() }
}



