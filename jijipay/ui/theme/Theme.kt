package com.example.jijipay.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val BlueColorScheme = lightColorScheme(
    primary = Color(0xFF1E88E5),
    onPrimary = Color.White,
    secondary = Color(0xFF90CAF9),
    onSecondary = Color.Black,
    background = Color(0xFFF1F8FF),
    onBackground = Color(0xFF0D47A1),
    surface = Color.White,
    onSurface = Color(0xFF1E1E1E)
)

@Composable
fun JijipayTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = BlueColorScheme,
        typography = androidx.compose.material3.Typography(),
        content = content
    )
}
