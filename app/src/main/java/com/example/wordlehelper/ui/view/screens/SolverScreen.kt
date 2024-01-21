package com.example.wordlehelper.ui.view.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.wordlehelper.ui.viewmodel.SolverViewModel
import java.util.Locale

@Composable
fun SolverScreen(
    navController: NavHostController,
    viewModel: SolverViewModel,
    isDarkTheme: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ){
        val context = LocalContext.current

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            viewModel.rows.forEachIndexed { index, word ->
                WordRow(
                    word = word,
                    isActive = viewModel.rows[index].length < 5 && viewModel.rows.take(index).all { it.length == 5 },
                    onWordChange = { newWord ->
                        viewModel.rows[index] = newWord.uppercase(Locale.getDefault()).take(5)
                    },
                    onEnter = {
                        if(word.length == 5 && isValidWord(word)){
                            Toast.makeText(context, "Word entered: $word", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(context, "Invalid word: $word", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = viewModel.rowColors[index],
                    onColorChange = { squareIndex, color ->
                        viewModel.updateSquareColor(index, squareIndex, color)
                    }
                )
            }

            Button(
                onClick = {/*TODO: Calculate button functionality */},
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "Calculate")
            }
        }
    }
}

//Row of square input fields
@Composable
fun WordRow(
    word: String,
    isActive: Boolean,
    onWordChange: (String) -> Unit,
    onEnter: () -> Unit,
    colors: List<Color>,
    onColorChange: (Int, Color) -> Unit
) {
    val focusRequesters = remember { List(5) { FocusRequester() } }

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        (0 until 5).forEach { index ->
            if (isActive) {
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
                    initialColor = MaterialTheme.colors.surface,
                    onColorSelected = { newColor ->
                        onColorChange(index, newColor)
                    }
                )
            }
        }
    }
}

fun isValidWord(word: String): Boolean {
    // TODO: Implement word validation logic
    return word.length == 5
}

//Inactive square with a dropdown to change its color
@Composable
fun ColorSelectableSquare(
    letter: String,
    initialColor: Color,
    onColorSelected: (Color) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedColor by remember { mutableStateOf(initialColor) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(48.dp)
            .background(selectedColor, shape = RoundedCornerShape(4.dp))
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
