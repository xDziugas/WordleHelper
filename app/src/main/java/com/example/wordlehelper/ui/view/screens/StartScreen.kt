package com.example.wordlehelper.ui.view.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.wordlehelper.R
import com.example.wordlehelper.ui.navigation.MainDestinations

@Composable
fun StartScreen(
    navController: NavHostController,
    isDarkTheme: Boolean,
    toggleTheme: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ){
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(animationSpec = tween(1000)),
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(
                        id = R.drawable.boxes
                    ),
                    contentDescription = "Icon"
                )

                Spacer(modifier = Modifier.height(48.dp))

                Button(
                    onClick = {
                        navController.navigate(MainDestinations.SOLVER_SCREEN)
                    },
                    modifier = Modifier
                        .size(
                            width = 200.dp,
                            height = 50.dp
                        )
                ){
                    Text("Wordle Solver")
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        navController.navigate(MainDestinations.SIMULATOR_SCREEN)
                    },
                    modifier = Modifier
                        .size(
                            width = 200.dp,
                            height = 50.dp
                        )
                ){
                    Text("Play Wordle")
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        navController.navigate(MainDestinations.INFO_SCREEN)
                    },
                    modifier = Modifier
                        .size(
                            width = 200.dp,
                            height = 50.dp
                        )
                ){
                    Text("Information")
                }
            }

            IconButton(
                onClick = {
                  toggleTheme()
                },
                modifier = Modifier
                    .padding(12.dp)
                    .size(42.dp)
                    .offset(x = calculateOffsetX())
            ){
                Icon(
                    painter = painterResource(
                        id = R.drawable.change_theme
                    ),
                    contentDescription = "Change Theme",
                    tint = MaterialTheme.colors.onBackground
                )
            }
        }
    }
}

@Composable
fun calculateOffsetX(): Dp {
    return LocalConfiguration.current.screenWidthDp.dp - 68.dp
}