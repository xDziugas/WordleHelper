package com.example.wordlehelper.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class WordRepository(private val context: Context) {

    private var words: List<String> = emptyList()

    suspend fun loadWords(): List<String> {
        withContext(Dispatchers.IO) {
            words = readWordsFromFile("Words.txt")
        }.also {
            return words
        }
    }

    suspend fun loadAnswers(): List<String> {
        withContext(Dispatchers.IO) {
            if(words.isEmpty()){
                words = readWordsFromFile("Answers.txt")
            }
        }.also {
            return words
        }
    }

    suspend fun getRandomWord(): String {
        return if(words.isEmpty()){
            loadAnswers().random()
        }else{
            words.random()
        }
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