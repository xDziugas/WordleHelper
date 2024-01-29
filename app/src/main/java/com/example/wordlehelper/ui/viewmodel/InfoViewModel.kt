package com.example.wordlehelper.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wordlehelper.repository.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor(
    private val wordRepository: WordRepository
) : ViewModel() {
    private val _possibleAnswers = MutableStateFlow<List<String>>(emptyList())
    val possibleAnswers: StateFlow<List<String>> = _possibleAnswers

    private val _allAnswers = MutableStateFlow<List<String>>(emptyList())
    val allAnswers: StateFlow<List<String>> = _allAnswers

    init {
        loadPossibleAnswersAndWords()
    }

    private fun loadPossibleAnswersAndWords() {
        viewModelScope.launch {
            _possibleAnswers.value = wordRepository.loadAnswers()
            _allAnswers.value = wordRepository.loadWords()
        }
    }
}
