package com.example.wordlehelper.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ThemeViewModel : ViewModel() {
    var isDarkTheme by mutableStateOf(true)

    fun toggleTheme() {
        isDarkTheme = !isDarkTheme
    }
}