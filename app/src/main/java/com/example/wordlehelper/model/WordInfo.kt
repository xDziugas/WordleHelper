package com.example.wordlehelper.model

import androidx.compose.ui.graphics.Color
import com.google.common.math.BigIntegerMath.log2
import kotlin.math.ln
import kotlin.math.log2

data class WordInfo(
    val word: String,
    val infoScore: Float
)

fun filterPossibleAnswers(
    allWords: List<String>,
    rowsInputs: List<List<WordInput>> // List of rows, each row is a list of WordInput
): List<String> {
    var possibleAnswers = allWords

    for (inputs in rowsInputs) {
        // Filter out words based on the inputs from the current row.
        possibleAnswers = possibleAnswers.filter { word ->
            inputs.all { input ->
                when (input.state) {
                    LetterState.CORRECT -> word[input.position] == input.letter
                    LetterState.PRESENT -> word.contains(input.letter) && word[input.position] != input.letter
                    LetterState.ABSENT -> !word.contains(input.letter)
                }
            }
        }
    }

    return possibleAnswers
}


fun calculateInformationScores(
    possibleAnswers: List<String>
): List<WordInfo> {
    return possibleAnswers.map { word ->
        val score = calculateEntropy(word, possibleAnswers)
        WordInfo(word, score)
    }
}


fun findBestGuess(words: List<String>, currentInputs: List<List<WordInput>>): WordInfo {
    val filteredWords = filterPossibleAnswers(words, currentInputs)
    return filteredWords.map { word ->
        val infoScore = calculateEntropy(word, filteredWords)
        WordInfo(word, infoScore)
    }.maxByOrNull { it.infoScore } ?: WordInfo("", 0f)
}

fun calculateEntropy(word: String, possibleWords: List<String>): Float {
    val letterFrequencies = IntArray(26) // 26 letters in the alphabet

    possibleWords.forEach { possibleWord ->
        possibleWord.forEach { letter ->
            letterFrequencies[letter.uppercaseChar() - 'A']++
        }
    }

    val totalLetters = possibleWords.size * 5 // Total number of letters in all words
    return word.uppercase().toCharArray().distinct().sumOf { letter ->
        val frequency = letterFrequencies[letter - 'A'].toDouble() / totalLetters
        if (frequency > 0) -frequency * (ln(frequency) / ln(2.0)) else 0.0
    }.toFloat()
}

