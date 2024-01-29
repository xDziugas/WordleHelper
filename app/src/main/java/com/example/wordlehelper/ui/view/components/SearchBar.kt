package com.example.wordlehelper.ui.view.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle

@Composable
fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit
) {
    TextField(
        value = query,
        onValueChange = onQueryChanged,
        label = {
            Text(
                "Search Words",
                color = MaterialTheme.colors.surface
            )
        },
        textStyle = TextStyle(color = MaterialTheme.colors.surface),
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.textFieldColors(
            textColor = MaterialTheme.colors.surface,
            cursorColor = MaterialTheme.colors.surface,
            focusedIndicatorColor = MaterialTheme.colors.surface,
            unfocusedIndicatorColor = MaterialTheme.colors.surface,
            disabledIndicatorColor = MaterialTheme.colors.surface
        ),
    )
}