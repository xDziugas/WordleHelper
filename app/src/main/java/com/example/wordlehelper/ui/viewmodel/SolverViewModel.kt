package com.example.wordlehelper.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel

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

    fun updateSquareColor(rowIndex: Int, squareIndex: Int, color: Color) {
        rowColors[rowIndex][squareIndex] = color
    }
}