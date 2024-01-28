package com.example.wordlehelper.ui.view.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordlehelper.ui.viewmodel.SimulatorViewModel
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

@Composable
fun GameGrid(
    context: Context,
    viewModel: SimulatorViewModel
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        viewModel.rows.forEachIndexed { rowIndex, row ->
            SimulatorWordRow(
                word = row.text,
                isActive = row.text.length < 5 && viewModel.rows.take(rowIndex).all { it.text.length == 5 },
                onWordChange = { newWord ->
                    viewModel.updateWord(rowIndex, newWord)
                },
                viewModel = viewModel,
                rowIndex = rowIndex
            )
        }
    }
}