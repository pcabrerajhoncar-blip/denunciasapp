package com.PeralesCabrera.denunciasapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/** Color distintivo de cada tipo de denuncia. */
fun colorDeTipo(tipo: String): Color = when (tipo.uppercase()) {
    "ROBO" -> Color(0xFFD97706)
    "ASALTO" -> Color(0xFFC62828)
    "VANDALISMO" -> Color(0xFF6A1B9A)
    else -> Color(0xFF546E7A)
}

/** "2026-10-28T09:00:00" -> "2026-10-28 09:00" */
fun formatearFecha(fecha: String): String = fecha.replace('T', ' ').take(16)

/** Etiqueta redondeada: Pendiente (ámbar) / Atendida (verde). */
@Composable
fun EtiquetaEstado(estado: Int, modifier: Modifier = Modifier) {
    val pendiente = estado == 1
    Surface(
        shape = RoundedCornerShape(50),
        color = if (pendiente) AmbarClaro else VerdeClaro,
        modifier = modifier
    ) {
        Text(
            text = if (pendiente) "Pendiente" else "Atendida",
            color = if (pendiente) AmbarOscuro else Verde,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

/** Tarjeta de una denuncia (se usa en la lista y en la búsqueda). */
@Composable
fun DenunciaItem(
    denuncia: Denuncia,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorTipo = colorDeTipo(denuncia.tipo)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            // Franja de color según el tipo
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(colorTipo)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = denuncia.tipo,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = colorTipo,
                        modifier = Modifier.weight(1f)
                    )
                    EtiquetaEstado(estado = denuncia.estado)
                }
                Text(
                    text = denuncia.direccion,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = denuncia.descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "#${denuncia.id} · DNI ${denuncia.dni} · ${formatearFecha(denuncia.fecha)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/** Mensaje centrado para estados vacíos. */
@Composable
fun MensajeVacio(texto: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = texto,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
