package com.example.wordlehelper.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wordlehelper.model.RowState
import com.example.wordlehelper.repository.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SimulatorViewModel @Inject constructor(
    private val wordRepository: WordRepository
) : ViewModel() {
    private val _currentWord = MutableStateFlow("")
    val currentWord: StateFlow<String> get() = _currentWord

    private val _usedLetters = mutableStateOf(setOf<Char>())
    val usedLetters: State<Set<Char>> = _usedLetters

    var rows = mutableStateListOf<RowState>()

    init {
        //TODO: Optimize this
        restartGame()
        initializeRows()
    }

    private fun initializeRows() {
        val initialRow = RowState("", MutableList(5){ Color.Gray})
        rows.clear()
        repeat(6) {
            rows.add(initialRow)
        }
    }

    private fun markLetterAsUsed(letter: Char) {
        _usedLetters.value = _usedLetters.value + letter
    }

    private fun updateUsedLetters(word: String) {
        word.forEach { char ->
            markLetterAsUsed(char)
        }
    }

    fun updateWord(rowIndex: Int, word: String) {
        val newText = word.uppercase(Locale.getDefault()).take(5)
        val updatedRow = rows[rowIndex].copy(text = newText)
        rows[rowIndex] = updatedRow
    }

    fun checkWordAndColorize(rowIndex: Int) {
        val enteredWord = rows[rowIndex].text
        val target = currentWord.value

        if (enteredWord.length == 5) {
            val colorized = colorizeWord(enteredWord, target)
            updateRowColors(rowIndex, colorized)
        }
    }

    private fun colorizeWord(enteredWord: String, targetWord: String): List<Color> {
        val enteredW = enteredWord.uppercase()
        val targetW = targetWord.uppercase()

        val resultColors = MutableList(5) { Color.Gray } // Default to gray (absent)
        val targetLetterCounts = targetW.groupingBy { it }.eachCount().toMutableMap()

        updateUsedLetters(enteredW)

        // First pass: Mark correct positions (green)
        enteredW.forEachIndexed { index, char ->
            if (targetW[index] == char) {
                resultColors[index] = Color.Green
                targetLetterCounts[char] = targetLetterCounts[char]!! - 1
            }
        }

        // Second pass: Mark present but wrong position (yellow)
        enteredW.forEachIndexed { index, char ->
            if (char in targetLetterCounts.keys && targetLetterCounts[char]!! > 0 && resultColors[index] != Color.Green) {
                resultColors[index] = Color.Yellow
                targetLetterCounts[char] = targetLetterCounts[char]!! - 1
            }
        }

        return resultColors
    }

    private fun updateRowColors(rowIndex: Int, colors: List<Color>) {
        val updatedRow = rows[rowIndex].copy(colors = colors.toMutableList())
        rows[rowIndex] = updatedRow
    }

    fun restartGame() {
        // Clear board and reset game state
        viewModelScope.launch {
            _currentWord.value = wordRepository.getRandomWord()
            clearBoard()
            clearUsedLetters()
        }
    }

    private fun clearUsedLetters() {
        _usedLetters.value = emptySet()
    }

    private fun clearBoard() {
        rows.replaceAll { rowState ->
            rowState.copy(
                text = "",
                colors = MutableList(5) { Color.Unspecified }
            )
        }
    }

}