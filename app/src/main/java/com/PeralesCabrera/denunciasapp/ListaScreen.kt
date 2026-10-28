package com.PeralesCabrera.denunciasapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ListaScreen(
    denuncias: List<Denuncia>,
    cargando: Boolean,
    hayMas: Boolean,
    onDenunciaClick: (Denuncia) -> Unit,
    onCargarMas: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        // El espacio inferior evita que el botón flotante tape la última tarjeta
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (denuncias.isNotEmpty()) {
            item {
                Text(
                    text = "${denuncias.size} denuncias cargadas",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        items(denuncias, key = { it.id }) { denuncia ->
            DenunciaItem(denuncia = denuncia, onClick = { onDenunciaClick(denuncia) })
        }

        // Pie de la lista: cargar los 5 siguientes
        item {
            when {
                cargando -> Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }

                hayMas -> OutlinedButton(
                    onClick = onCargarMas,
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Cargar 5 siguientes") }

                denuncias.isEmpty() -> MensajeVacio(
                    "Aún no hay denuncias registradas.\nToca \"+ Nueva denuncia\" para crear la primera."
                )

                else -> MensajeVacio("No hay más denuncias")
            }
        }
    }
}
