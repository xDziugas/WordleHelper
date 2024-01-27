package com.example.wordlehelper.ui.view.components

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.wordlehelper.ui.viewmodel.SolverViewModel

@Composable
fun GameGrid(
    context: Context,
    viewModel: SolverViewModel
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        viewModel.rows.forEachIndexed { rowIndex, row ->
            WordRow(
                word = row.text,
                isActive = row.text.length < 5 && viewModel.rows.take(rowIndex).all { it.text.length == 5 },
                onWordChange = { newWord ->
                    viewModel.updateWord(rowIndex, newWord)
                },
                onColorChange = { squareIndex, color ->
                    viewModel.updateSquareColor(rowIndex, squareIndex, color)
                }
            )
        }
    }
}