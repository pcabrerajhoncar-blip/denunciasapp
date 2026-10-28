package com.PeralesCabrera.denunciasapp

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Paleta de seguridad ciudadana: azul marino + ámbar, con verde para "atendida".
val Azul = Color(0xFF1F3A5F)
val AzulClaro = Color(0xFFD6E2F3)
val Ambar = Color(0xFFF59E0B)
val AmbarClaro = Color(0xFFFFF1D6)
val AmbarOscuro = Color(0xFF8A5A00)
val Verde = Color(0xFF2E7D32)
val VerdeClaro = Color(0xFFDDF1DE)
val Fondo = Color(0xFFF4F6F9)

private val EsquemaClaro = lightColorScheme(
    primary = Azul,
    onPrimary = Color.White,
    primaryContainer = AzulClaro,
    onPrimaryContainer = Azul,
    secondary = Ambar,
    onSecondary = Color(0xFF3B2A00),
    secondaryContainer = AzulClaro,
    onSecondaryContainer = Azul,
    background = Fondo,
    onBackground = Color(0xFF1B1F27),
    surface = Color.White,
    onSurface = Color(0xFF1B1F27),
    surfaceVariant = Color(0xFFE6EAF0),
    onSurfaceVariant = Color(0xFF4A5568),
    error = Color(0xFFC62828),
    onError = Color.White
)

@Composable
fun DenunciasTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaClaro,
        content = content
    )
}
