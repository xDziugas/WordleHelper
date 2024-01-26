package com.example.wordlehelper.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlin.math.log2

data class WordInfo(
    val word: String,
    val infoScore: Float
)

fun filterPossibleAnswers(
    allWords: List<String>,
    rowsInputs: List<List<WordInput>>
): List<String> {
    return allWords.filter { word ->
        rowsInputs.flatten().all { input ->
            when (input.state) {
                LetterState.CORRECT -> word[input.position] == input.letter.lowercaseChar()
                LetterState.PRESENT -> input.letter.lowercaseChar() in word && word[input.position] != input.letter.lowercaseChar()
                LetterState.ABSENT -> input.letter.lowercaseChar() !in word
            }
        }
    }
}

suspend fun calculateInformationScores(
    possibleAnswers: List<String>
): List<WordInfo> {
    return coroutineScope {
        possibleAnswers.map { word ->
            async(Dispatchers.Default) {
                val score = calculateEntropy(word, possibleAnswers)
                WordInfo(word, score)
            }
        }.awaitAll()
    }
}

fun calculateEntropy(guess: String, possibleWords: List<String>): Float {
    val patternCounts = mutableMapOf<String, Int>()

    // Generate pattern counts for each possible word
    possibleWords.forEach { word ->
        val pattern = getPattern(guess, word)
        patternCounts[pattern] = patternCounts.getOrDefault(pattern, 0) + 1
    }

    // Calculate the entropy based on these pattern counts
    val totalPatterns = possibleWords.size.toFloat()
    val entropy = patternCounts.values.fold(0f) { acc, count ->
        val probability = count.toFloat() / totalPatterns
        acc + if (probability > 0) -probability * (log2(probability)) else 0f
    }

    return entropy
}

fun getPattern(guess: String, word: String): String {
    val pattern = CharArray(guess.length) { 'A' } // Default to 'Absent'
    val wordLetterCount = word.groupingBy { it }.eachCount().toMutableMap()

    // First pass for correct (C)
    guess.forEachIndexed { index, c ->
        if (word[index] == c) {
            pattern[index] = 'C'
            wordLetterCount[c] = wordLetterCount[c]?.minus(1) ?: 0
        }
    }

    // Second pass for present (P)
    guess.forEachIndexed { index, c ->
        if (pattern[index] == 'A' && wordLetterCount.getOrDefault(c, 0) > 0) {
            pattern[index] = 'P'
            wordLetterCount[c] = wordLetterCount[c]?.minus(1) ?: 0
        }
    }

    return String(pattern)
}




