package com.example.wordlehelper.ui.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.navigation.NavHostController
import com.example.wordlehelper.R
import com.example.wordlehelper.ui.navigation.MainDestinations
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ){
        Image(
            painter = painterResource(id = R.drawable.splash),
            contentDescription = "Logo"
        )
        LaunchedEffect(key1 = true){
            delay(3000)
            navController.navigate(MainDestinations.START_SCREEN){
                popUpTo(MainDestinations.SPLASH_SCREEN){
                    inclusive = true
                }
            }
        }
    }
}
