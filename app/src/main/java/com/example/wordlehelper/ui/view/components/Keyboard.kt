package com.example.wordlehelper.ui.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordlehelper.ui.viewmodel.SimulatorViewModel

@Composable
fun Keyboard(
    viewModel: SimulatorViewModel,
) {
    val usedLetters by viewModel.usedLetters
    val rowsOfKeys = listOf("QWERTYUIOP", "ASDFGHJKL", "ZXCVBNM")

    Column {
        rowsOfKeys.forEach { row ->
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                row.forEach { letter ->
                    val isUsed = letter in usedLetters
                    KeyButton(letter = letter, isUsed = isUsed)
                }
            }
        }
    }
}

@Composable
fun KeyButton(letter: Char, isUsed: Boolean) {
    val buttonWidth = 36.dp
    val buttonHeight = 44.dp

    Button(
        onClick = { },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (isUsed) Color.Gray else MaterialTheme.colors.primary
        ),
        modifier = Modifier
            .width(buttonWidth)
            .height(buttonHeight)
            .padding(1.dp)
    ) {
        Text(
            text = letter.toString(),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                fontSize = 16.sp,
                color = MaterialTheme.colors.onSurface
            )
        )
    }
}