package com.example.wordlehelper.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wordlehelper.model.LetterState
import com.example.wordlehelper.model.WordInfo
import com.example.wordlehelper.model.WordInput
import com.example.wordlehelper.model.calculateInformationScores
import com.example.wordlehelper.model.filterPossibleAnswers
import com.example.wordlehelper.model.findBestGuess
import com.example.wordlehelper.ui.view.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

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

    private val _answers = MutableLiveData<List<String>>()
    val answers: LiveData<List<String>> = _answers

    private val _words = MutableStateFlow<List<String>>(emptyList())
    val words = _words.asStateFlow()

    private val _topGuesses = MutableStateFlow<List<WordInfo>>(emptyList())
    val topGuesses = _topGuesses.asStateFlow()

    fun setWords(words: List<String>) {
        _words.value = words
        // Call here any logic that needs to be triggered upon words being set.
    }

    fun updateSquareColor(rowIndex: Int, squareIndex: Int, color: Color) {
        rowColors[rowIndex][squareIndex] = color
    }

    fun loadWordsAndAnswers(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val loadedWords = readWordsFromFile(context, "Words.txt")
            val loadedAnswers = readWordsFromFile(context, "Answers.txt")
            withContext(Dispatchers.Main) {
                _words.value = loadedWords
                _answers.value = loadedAnswers
            }
            println("Loaded words: ${_words.value[5]}")
        }
        println("Loaded words2: ${_words.value[5]}")
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
        Log.d("SolverViewModel", "Processing Words - Current inputs: $currentInputs")

        val wordsList = _words.value
        Log.d("SolverViewModel", "Words List (From StateFlow): $wordsList")

        if (wordsList.isNullOrEmpty()) {
            Log.d("SolverViewModel", "Words list is empty or null")
            return
        }

        val filteredWords = filterPossibleAnswers(wordsList, currentInputs)
        Log.d("SolverViewModel", "Filtered Words: $filteredWords")

        val infoScores = calculateInformationScores(filteredWords)
        Log.d("SolverViewModel", "Info Scores: $infoScores")

        val topGuesses = infoScores.sortedByDescending { it.infoScore }.take(10)
        _topGuesses.value = topGuesses
    }

}

