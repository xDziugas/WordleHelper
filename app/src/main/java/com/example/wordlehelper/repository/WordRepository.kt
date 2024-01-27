package com.example.wordlehelper.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class WordRepository(private val context: Context) {

    suspend fun loadWords(): List<String> = withContext(Dispatchers.IO) {
        readWordsFromFile("Words.txt")
    }

    suspend fun loadAnswers(): List<String> = withContext(Dispatchers.IO) {
        readWordsFromFile("Answers.txt")
    }

    private fun readWordsFromFile(fileName: String): List<String> {
        return try {
            context.assets.open(fileName).bufferedReader().useLines { lines ->
                lines.filter { it.isNotBlank() }.toList()
            }
        } catch (e: IOException) {
            emptyList()
        }
    }
}