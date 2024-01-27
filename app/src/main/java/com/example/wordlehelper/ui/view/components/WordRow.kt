package com.example.wordlehelper.ui.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
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

@Composable
fun WordRow(
    word: String,
    isActive: Boolean,
    onWordChange: (String) -> Unit,
    onColorChange: (Int, Color) -> Unit
) {
    val focusRequesters = remember { List(5) { FocusRequester() } }

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        (0 until 5).forEach { index ->
            if (isActive) {
                println("word text: $word")
                TextField(
                    value = if (index < word.length) word[index].toString() else "",
                    onValueChange = { input ->
                        if (input.length <= 1) {
                            val newWord = buildString {
                                append(word.take(index))
                                append(input)
                                append(word.drop(index + 1))
                            }
                            onWordChange(newWord.take(5))

                            // Move to next square
                            if (index < 4) {
                                focusRequesters[index + 1].requestFocus()
                            }
                        }
                    },
                    textStyle = TextStyle(
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        color = MaterialTheme.colors.background
                    ),
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.LightGray),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = if (index == 4) ImeAction.Done else ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        if (index < 4) focusRequesters[index + 1].requestFocus()
                    }),
                    modifier = Modifier
                        .size(48.dp)
                        .background(MaterialTheme.colors.surface, RoundedCornerShape(4.dp))
                        .focusRequester(focusRequesters[index]),
                )
            }
            else {
                // Non-active square
                ColorSelectableSquare(
                    letter = word.getOrNull(index)?.toString() ?: "",
                    initialColor = MaterialTheme.colors.onSurface,
                    onColorSelected = { newColor ->
                        onColorChange(index, newColor)
                    },
                    color = MaterialTheme.colors.surface
                )
            }
        }
    }
}