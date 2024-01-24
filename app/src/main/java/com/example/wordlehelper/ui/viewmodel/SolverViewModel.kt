package com.example.wordlehelper.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wordlehelper.model.LetterState
import com.example.wordlehelper.model.WordData
import com.example.wordlehelper.model.WordInfo
import com.example.wordlehelper.model.WordInput
import com.example.wordlehelper.model.calculateInformationScores
import com.example.wordlehelper.model.filterPossibleAnswers
import com.example.wordlehelper.model.findBestGuess
import com.example.wordlehelper.ui.view.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.concurrent.Flow.Subscriber

class SolverViewModel : ViewModel(){
    var rows = mutableStateListOf("", "", "", "", "", "")
    var rowColors = mutableStateListOf(
        MutableList(5) { Color.Unspecified },
        MutableList(5) { Color.Unspecified },
        MutableList(5) { Color.Unspecified },
        MutableList(5) { Color.Unspecified },
        MutableList(5) { Color.Unspecified },
        MutableList(5) { Color.Unspecified }
    )

    init {
        calculateTopGuesses(emptyList())
    }

    private val _topGuesses = MutableStateFlow<List<WordInfo>>(emptyList())
    val topGuesses = _topGuesses.asStateFlow()

    private val _wordData = MutableStateFlow(WordData(emptyList(), emptyList()))
    val wordData: StateFlow<WordData> = _wordData

    private val _isDataLoaded = MutableStateFlow(false)
    val isDataLoaded: StateFlow<Boolean> = _isDataLoaded

    fun loadWordsAndAnswers(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val loadedWords = readWordsFromFile(context, "Words.txt")
            val loadedAnswers = readWordsFromFile(context, "Answers.txt")
            withContext(Dispatchers.Main) {
                _wordData.value.words = loadedWords
                _wordData.value.answers = loadedAnswers
                calculateTopGuesses(emptyList())
            }
        }
    }

    private fun readWordsFromFile(context: Context, fileName: String): List<String> {
        return try {
            context.assets.open(fileName).bufferedReader().useLines { lines ->
                lines.filter { it.isNotBlank() }.toList().also {
                    Log.d("SolverViewModel", "Read ${it.size} words from $fileName")
                }
            }
        } catch (e: IOException) {
            Log.e("SolverViewModel", "Error reading $fileName", e)
            emptyList()
        }
    }

    fun onCalculatePressed() {
        val currentInputs = convertBoardToInputs()
        processWords(currentInputs)
    }

    private fun convertBoardToInputs(): List<List<WordInput>> {
        // Convert the current state of the board into a list of lists of WordInput
        return rows.mapIndexed { rowIndex, word ->
            word.mapIndexedNotNull { charIndex, char ->
                if (char.isLetter()) { // Ensure only valid letters are included
                    val state = when (rowColors[rowIndex][charIndex]) {
                        Color.Green -> LetterState.CORRECT
                        Color.Yellow -> LetterState.PRESENT
                        Color.Gray -> LetterState.ABSENT
                        else -> LetterState.ABSENT
                    }
                    WordInput(char.uppercaseChar(), charIndex, state)
                } else {
                    null
                }
            }
        }
    }

    private fun processWords(currentInputs: List<List<WordInput>>) {
        val wordsList = _wordData.value.words

        if (wordsList.isNullOrEmpty()) {
            Log.d("SolverViewModel", "Words list is empty or null")
            return
        }

        val filteredWords = filterPossibleAnswers(wordsList, currentInputs)
        Log.d("SolverViewModel", "Filtered Words: $filteredWords")
        Log.d("SolverViewModel", "Filtered Words size: ${filteredWords.size}")

        val infoScores = calculateInformationScores(filteredWords)
        Log.d("SolverViewModel", "Info Scores: $infoScores")

        val topGuesses = infoScores.sortedByDescending { it.infoScore }.take(10)
        _topGuesses.value = topGuesses
    }

    fun updateSquareColor(rowIndex: Int, squareIndex: Int, color: Color) {
        rowColors[rowIndex][squareIndex] = color
    }

    fun calculateInitialTopGuesses() {
        if (_isDataLoaded.value) {
            calculateTopGuesses(emptyList()) // Calculate with no filters
        }
    }

    private fun calculateTopGuesses(inputs: List<List<WordInput>>) {
        try {
            val filteredWords = if (inputs.isNotEmpty()) {
                filterPossibleAnswers(_wordData.value.words, inputs)
            } else {
                _wordData.value.words
            }

            val infoScores = calculateInformationScores(filteredWords)
            val topGuesses = infoScores.sortedByDescending { it.infoScore }.take(10)
            _topGuesses.value = topGuesses

            Log.d("SolverViewModel", "Top Guesses: $topGuesses")
            Log.d("SolverViewModel", "_topGuesses: ${_topGuesses.value}")

        } catch (e: Exception) {
            Log.e("SolverViewModel", "Error in calculateTopGuesses: ${e.message}")
            // Handle the exception or set a default value
        }
    }

}

