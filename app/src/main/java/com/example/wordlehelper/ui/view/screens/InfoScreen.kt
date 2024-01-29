package com.example.wordlehelper.ui.view.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordlehelper.ui.view.components.SearchBar
import com.example.wordlehelper.ui.viewmodel.InfoViewModel

@Composable
fun InfoScreen(
    viewModel: InfoViewModel
) {
    val possibleAnswers by viewModel.possibleAnswers.collectAsState()
    val allWords by viewModel.allAnswers.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedList by remember { mutableStateOf("Possible Answers") }
    val options = listOf("Possible Answers", "All Words")

    val filteredWords by remember(selectedList, searchQuery) {
        derivedStateOf {
            getFilteredWords(
                selectedList = selectedList,
                possibleAnswers = possibleAnswers,
                allWords = allWords,
                searchQuery = searchQuery
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .weight(2f)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ){
            Text(
                text = "Wordle Information",
                style = MaterialTheme.typography.h4,
                color = MaterialTheme.colors.surface,
            )

            Spacer(modifier = Modifier.height(32.dp))

            DropdownSelector(
                options = options,
                selectedOption = selectedList,
                onOptionSelected = { selectedList = it }
            )

            Spacer(modifier = Modifier.height(32.dp))

            SearchBar(
                query = searchQuery,
                onQueryChanged = { searchQuery = it }
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .weight(3f)
                .padding(16.dp)
        ) {
            items(filteredWords) { word ->
                Card(
                    backgroundColor = MaterialTheme.colors.surface,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = word.uppercase(),
                        style = MaterialTheme.typography.body1.copy(
                            color = MaterialTheme.colors.background,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun DropdownSelector(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                MaterialTheme.colors.surface,
                shape = RoundedCornerShape(4.dp)
            )
    ) {
        Text(
            text = selectedOption,
            style = TextStyle(color = MaterialTheme.colors.onSurface),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .padding(16.dp)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(onClick = {
                    onOptionSelected(option)
                    expanded = false
                }) {
                    Text(
                        text = option,
                        style = TextStyle(
                            color = MaterialTheme.colors.background
                        )
                    )
                }
            }
        }
    }
}

fun getFilteredWords(
    selectedList: String,
    possibleAnswers: List<String>,
    allWords: List<String>,
    searchQuery: String
): List<String> {
    Log.d("getFilteredWords",
        "selectedList: $selectedList, " +
                "searchQuery: $searchQuery, " +
                "possibleAnswers: ${possibleAnswers.size}, " +
                "allWords: ${allWords.size}"
    )
    return when (selectedList) {
        "Possible Answers" -> possibleAnswers.filter { it.contains(searchQuery, ignoreCase = true) }
        "All Words" -> allWords.filter { it.contains(searchQuery, ignoreCase = true) }
        else -> emptyList()
    }
}
