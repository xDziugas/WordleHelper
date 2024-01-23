package com.example.wordlehelper.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.wordlehelper.ui.navigation.WordleHelperNavGraph
import com.example.wordlehelper.ui.theme.WordleHelperTheme
import com.example.wordlehelper.ui.view.screens.StartScreen
import com.example.wordlehelper.ui.viewmodel.SolverViewModel
import com.example.wordlehelper.ui.viewmodel.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            val themeViewModel: ThemeViewModel = viewModel()
            AppContent(themeViewModel)
        }
        val viewModel: SolverViewModel by viewModels()

        viewModel.loadWordsAndAnswers(this)
    }
}

@Composable
fun AppContent(themeViewModel: ThemeViewModel){
    val navController = rememberNavController()

    WordleHelperTheme(darkTheme = themeViewModel.isDarkTheme) {
        WordleHelperNavGraph(
            navController = navController,
            themeViewModel = themeViewModel
        )
    }
}

