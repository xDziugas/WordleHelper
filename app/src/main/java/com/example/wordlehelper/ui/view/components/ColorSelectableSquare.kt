package com.example.wordlehelper.ui.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ColorSelectableSquare(
    letter: String,
    initialColor: Color,
    onColorSelected: (Color) -> Unit,
    color: Color
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedColor by remember { mutableStateOf(initialColor) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(48.dp)
            .background(if(letter.isEmpty())color else selectedColor, shape = RoundedCornerShape(4.dp))
            .clickable { expanded = true }
            .border(1.dp, Color.Black, shape = RoundedCornerShape(4.dp))
    ) {
        Text(
            text = letter,
            style = TextStyle(
                fontSize = 16.sp,
                color = MaterialTheme.colors.background,
                textAlign = TextAlign.Center
            )
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            listOf(Color.Gray, Color.Yellow, Color.Green).forEach { color ->
                DropdownMenuItem(onClick = {
                    selectedColor = color
                    onColorSelected(color)
                    expanded = false
                }) {
                    Box(modifier = Modifier
                        .size(24.dp)
                        .background(color, shape = RoundedCornerShape(4.dp)))
                }
            }
        }
    }
}