package com.example.wordlehelper.ui.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.wordlehelper.ui.view.components.GameGrid
import com.example.wordlehelper.ui.viewmodel.SimulatorViewModel

@Composable
fun SimulatorScreen(viewModel: SimulatorViewModel) {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        GameGrid(
            context = context,
            viewModel = viewModel
        )
        Keyboard(viewModel::onKeyPress)
        RestartButton { viewModel.restartGame() }
    }
}

@Composable
fun GuessesGrid(guesses: List<String>) {
    // Layout for the guesses grid
}

@Composable
fun Keyboard(onKeyPress: (Char) -> Unit) {
    // Layout for the keyboard with buttons for each letter
}

@Composable
fun RestartButton(onRestart: () -> Unit) {
    Button(
        onClick = onRestart,
        modifier = Modifier.padding(16.dp)
    ) {
        Text(text = "Restart")
    }
}