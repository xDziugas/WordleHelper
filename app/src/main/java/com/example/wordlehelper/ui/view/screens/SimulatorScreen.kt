package com.example.wordlehelper.ui.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.wordlehelper.ui.view.components.GameGrid
import com.example.wordlehelper.ui.view.components.Keyboard
import com.example.wordlehelper.ui.viewmodel.SimulatorViewModel

@Composable
fun SimulatorScreen(
    viewModel: SimulatorViewModel
) {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        Spacer(modifier = Modifier.weight(1f))

        GameGrid(
            context = context,
            viewModel = viewModel
        )

        Spacer(modifier = Modifier.weight(1f))

        Keyboard(
            viewModel = viewModel
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ){
            RestartButton { viewModel.restartGame() }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
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