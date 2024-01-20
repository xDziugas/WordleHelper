package com.example.wordlehelper.ui.view.screens

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.wordlehelper.ui.navigation.MainDestinations

@Composable
fun StartScreen(navController: NavHostController) {
   Button(
       onClick = { navController.navigate(MainDestinations.SOLVER_SCREEN) }
   ){
       Text("Wordle Solver")
   }
}