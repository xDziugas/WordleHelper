package com.example.wordlehelper.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.wordlehelper.ui.navigation.WordleHelperNavGraph
import com.example.wordlehelper.ui.theme.WordleHelperTheme
import com.example.wordlehelper.ui.viewmodel.SimulatorViewModel
import com.example.wordlehelper.ui.viewmodel.SolverViewModel
import com.example.wordlehelper.ui.viewmodel.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            val themeViewModel: ThemeViewModel = viewModel()
            val solverViewModel: SolverViewModel by viewModels()
            val simulatorViewModel: SimulatorViewModel by viewModels()

            AppContent(
                themeViewModel,
                solverViewModel,
                simulatorViewModel
            )
        }
    }
}

@Composable
fun AppContent(
    themeViewModel: ThemeViewModel,
    solverViewModel: SolverViewModel,
    simulatorViewModel: SimulatorViewModel
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
            solverViewModel = solverViewModel,
            simulatorViewModel = simulatorViewModel
        )
    }
}

