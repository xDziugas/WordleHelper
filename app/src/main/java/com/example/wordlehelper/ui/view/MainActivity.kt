package com.example.wordlehelper.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.wordlehelper.repository.WordRepository
import com.example.wordlehelper.ui.navigation.WordleHelperNavGraph
import com.example.wordlehelper.ui.theme.WordleHelperTheme
import com.example.wordlehelper.ui.viewmodel.SolverViewModel
import com.example.wordlehelper.ui.viewmodel.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            val themeViewModel: ThemeViewModel = viewModel()
            val viewModel: SolverViewModel by viewModels()
            AppContent(themeViewModel, viewModel)
        }
    }
}

@Composable
fun AppContent(
    themeViewModel: ThemeViewModel,
    solverViewModel: SolverViewModel
){
    val navController = rememberNavController()

    LaunchedEffect(key1 = true) {
        //Use loadWordsAndAnswers to load all possible entries (x4)
        solverViewModel.loadAnswers()
    }

    WordleHelperTheme(darkTheme = themeViewModel.isDarkTheme) {
        WordleHelperNavGraph(
            navController = navController,
            themeViewModel = themeViewModel,
            solverViewModel = solverViewModel
        )
    }
}

