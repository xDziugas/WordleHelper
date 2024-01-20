package com.example.wordlehelper.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.wordlehelper.ui.view.screens.InfoScreen
import com.example.wordlehelper.ui.view.screens.SimulatorScreen
import com.example.wordlehelper.ui.view.screens.SolverScreen
import com.example.wordlehelper.ui.view.screens.SplashScreen
import com.example.wordlehelper.ui.view.screens.StartScreen

object MainDestinations {
    const val SPLASH_SCREEN = "splash"
    const val START_SCREEN = "start"
    const val SOLVER_SCREEN = "solver"
    const val SIMULATOR_SCREEN = "simulator"
    const val INFO_SCREEN = "info"
}

@Composable
fun WordleHelperNavGraph(navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = MainDestinations.SPLASH_SCREEN
    ){
        composable(MainDestinations.SPLASH_SCREEN){
            SplashScreen(navController)
        }
        composable(MainDestinations.START_SCREEN){
            StartScreen(navController)
        }
        composable(MainDestinations.SOLVER_SCREEN) {
            SolverScreen(navController)
        }
        composable(MainDestinations.SIMULATOR_SCREEN) {
            SimulatorScreen(navController)
        }
        composable(MainDestinations.INFO_SCREEN) {
            InfoScreen(navController)
        }
    }
}