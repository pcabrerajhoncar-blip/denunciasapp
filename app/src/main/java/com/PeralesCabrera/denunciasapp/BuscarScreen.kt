package com.PeralesCabrera.denunciasapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun BuscarScreen(
    buscando: Boolean,
    resultados: List<Denuncia>?,
    onBuscar: (String) -> Unit,
    onDenunciaClick: (Denuncia) -> Unit,
    modifier: Modifier = Modifier
) {
    var dni by rememberSaveable { mutableStateOf("") }
    var consultado by rememberSaveable { mutableStateOf("") }

    Column(modifier = modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = dni,
                    onValueChange = { nuevo ->
                        if (nuevo.length <= 8 && nuevo.all { it.isDigit() }) dni = nuevo
                    },
                    label = { Text("DNI (8 dígitos)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = {
                        consultado = dni
                        onBuscar(dni)
                    },
                    enabled = dni.length == 8 && !buscando,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Buscar")
                }
            }
        }

        when {
            buscando -> Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }

            resultados == null -> MensajeVacio("Ingresa un DNI de 8 dígitos y pulsa Buscar.")

            resultados.isEmpty() -> MensajeVacio("El DNI $consultado no tiene denuncias registradas.")

            else -> LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = if (resultados.size == 1) "1 denuncia del DNI $consultado"
                        else "${resultados.size} denuncias del DNI $consultado",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                items(resultados, key = { it.id }) { denuncia ->
                    DenunciaItem(denuncia = denuncia, onClick = { onDenunciaClick(denuncia) })
                }
            }
        }
    }
}
