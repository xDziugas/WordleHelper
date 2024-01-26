package com.example.wordlehelper.model

data class WordInput(
    val letter: Char,
    val position: Int,
    val state: LetterState
)

enum class LetterState {
    CORRECT, PRESENT, ABSENT
}
