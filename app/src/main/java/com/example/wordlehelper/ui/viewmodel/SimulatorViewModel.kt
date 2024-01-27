package com.example.wordlehelper.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wordlehelper.repository.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SimulatorViewModel @Inject constructor(
    private val wordRepository: WordRepository
) : ViewModel() {
    private val _currentWord = MutableStateFlow("")
    val currentWord: StateFlow<String> get() = _currentWord

    init {
        //TODO: Optimize this
        restartGame()
    }
    fun onKeyPress(key: Char) {
        // Add logic to handle key presses
        // Update guesses and used keys
    }

    fun restartGame() {
        // Clear guesses and reset game state
        viewModelScope.launch {
            _currentWord.value = wordRepository.getRandomWord()
            Log.d("SimulatorViewModel", "restartGame, random word: ${_currentWord.value}")
        }
    }

}