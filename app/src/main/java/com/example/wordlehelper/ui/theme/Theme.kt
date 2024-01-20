package com.example.wordlehelper.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val darkColorPalette = darkColors(
    primary = Green700,
    primaryVariant = Green900,
    secondary = Blue200,
    background = DarkBackground,
    surface = DarkSurface,
    error = RedError,
    onPrimary = White,
    onSecondary = Black,
    onBackground = White,
    onSurface = White,
    onError = White
)

private val lightColorPalette = lightColors(
    primary = Green500,
    primaryVariant = Green700,
    secondary = Blue700,
    background = LightBackground,
    surface = LightSurface,
    error = RedError,
    onPrimary = White,
    onSecondary = White,
    onBackground = Black,
    onSurface = Black,
    onError = Black
)

val Shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(0.dp)
)

val Typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
)

@Composable
fun WordleHelperTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        darkColorPalette
    } else {
        lightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}