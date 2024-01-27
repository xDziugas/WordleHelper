package com.example.wordlehelper.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wordlehelper.model.LetterState
import com.example.wordlehelper.model.RowState
import com.example.wordlehelper.model.WordData
import com.example.wordlehelper.model.WordInfo
import com.example.wordlehelper.model.WordInput
import com.example.wordlehelper.model.calculateInformationScores
import com.example.wordlehelper.model.filterPossibleAnswers
import com.example.wordlehelper.repository.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SolverViewModel @Inject constructor(private val wordRepository: WordRepository) : ViewModel() {

    var rows = mutableStateListOf<RowState>()

    init {
        initializeRows()
        calculateTopGuesses(emptyList())
    }

    private val _topGuesses = MutableStateFlow<List<WordInfo>>(emptyList())
    val topGuesses = _topGuesses.asStateFlow()

    private val _wordData = MutableStateFlow(WordData(emptyList(), emptyList()))
    val wordData: StateFlow<WordData> get() = _wordData

    private var initialWordScores: List<WordInfo>? = null

    private fun initializeRows() {
        val initialRow = RowState("", MutableList(5){ Color.Unspecified})
        rows.clear()
        repeat(6) {
            rows.add(initialRow)
        }
    }

    fun loadWordsAndAnswers() {
        viewModelScope.launch {
            val loadedAnswers = wordRepository.loadAnswers()
            val loadedWords = wordRepository.loadWords()
            _wordData.value.words = loadedWords
            _wordData.value.answers = loadedAnswers
            calculateTopGuesses(emptyList())
        }
    }

    fun loadAnswers() {
        viewModelScope.launch {
            val loadedAnswers = wordRepository.loadAnswers()
            _wordData.value.answers = loadedAnswers
            calculateTopGuesses(emptyList())
        }
    }

    fun onCalculatePressed() {
        val currentInputs = convertBoardToInputs()
        calculateTopGuesses(currentInputs)
    }

    private fun convertBoardToInputs(): List<List<WordInput>> {
        // Convert the current state of the board into a list of lists of WordInput
        return rows.map { rowState ->
            rowState.text.mapIndexedNotNull { index, char ->
                if (char.isLetter()) {
                    val state = when (rowState.colors[index]) {
                        Color.Green -> LetterState.CORRECT
                        Color.Yellow -> LetterState.PRESENT
                        Color.Gray -> LetterState.ABSENT
                        else -> LetterState.ABSENT
                    }
                    WordInput(char.uppercaseChar(), index, state)
                } else {
                    null
                }
            }
        }
    }

    //Trigger recomposition???
    fun updateSquareColor(rowIndex: Int, squareIndex: Int, color: Color) {
        val row = rows[rowIndex]
        if (squareIndex < row.colors.size) {
            val updatedColors = row.colors.toMutableList().apply {
                this[squareIndex] = color
            }
            rows[rowIndex] = row.copy(colors = updatedColors)
        } else {
            val extendedColors = row.colors.toMutableList().apply {
                addAll(List(squareIndex - size + 1) { Color.Unspecified })
                this[squareIndex] = color
            }
            rows[rowIndex] = row.copy(colors = extendedColors)
        }
    }

    //Trigger recomposition
    fun updateWord(rowIndex: Int, word: String) {
        val newText = word.uppercase(Locale.getDefault()).take(5)
        val updatedRow = rows[rowIndex].copy(text = newText)
        rows[rowIndex] = updatedRow
    }

    fun clearBoard() {
        rows.replaceAll { rowState ->
            rowState.copy(
                text = "",
                colors = MutableList(5) { Color.Unspecified }
            )
        }
        onCalculatePressed()
    }

    private fun calculateTopGuesses(inputs: List<List<WordInput>>) {
        viewModelScope.launch {
            try{
                if (inputs.all { it.isEmpty() } && initialWordScores != null) {
                    _topGuesses.value = initialWordScores!!.sortedByDescending { it.infoScore }.take(10)
                } else {
                    val filteredWords = filterPossibleAnswers(_wordData.value.answers, inputs)
                    calculateInformationScores(filteredWords).let { infoScores ->
                        val topGuesses = infoScores.sortedByDescending { it.infoScore }.take(10)
                        _topGuesses.value = topGuesses
                    }
                    if(initialWordScores == null){
                        initialWordScores = _topGuesses.value
                    }
                }
            }catch (e: Exception){
                Log.e("SolverViewModel", "Error calculating top guesses", e)
            }
        }
    }
}

