package com.PeralesCabrera.denunciasapp

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DenunciasApp(vm: DenunciasViewModel) {
    val snackbarHostState = remember { SnackbarHostState() }

    // Muestra los mensajes (éxito / error) que publica el ViewModel
    val mensaje = vm.mensaje
    LaunchedEffect(mensaje) {
        if (mensaje != null) {
            snackbarHostState.showSnackbar(mensaje)
            vm.limpiarMensaje()
        }
    }

    // El botón "atrás" del celular vuelve a la pantalla anterior
    BackHandler(enabled = vm.pantalla != Pantalla.Lista) { vm.volver() }

    val titulo = when (vm.pantalla) {
        Pantalla.Lista -> "Denuncias"
        Pantalla.Nueva -> "Nueva denuncia"
        Pantalla.Detalle -> "Detalle"
        Pantalla.Buscar -> "Buscar por DNI"
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(titulo, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    if (vm.pantalla != Pantalla.Lista) {
                        TextButton(onClick = { vm.volver() }) {
                            Text(
                                text = "←",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.White
                            )
                        }
                    }
                },
                actions = {
                    if (vm.pantalla == Pantalla.Lista) {
                        TextButton(onClick = { vm.irA(Pantalla.Buscar) }) {
                            Text("Buscar por DNI", color = Color.White)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Azul,
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            if (vm.pantalla == Pantalla.Lista) {
                ExtendedFloatingActionButton(
                    onClick = { vm.irA(Pantalla.Nueva) },
                    containerColor = Ambar,
                    contentColor = Color(0xFF3B2A00)
                ) {
                    Text("+ Nueva denuncia", fontWeight = FontWeight.Bold)
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        val contenido = Modifier.padding(padding)

        when (vm.pantalla) {
            Pantalla.Lista -> ListaScreen(
                denuncias = vm.denuncias,
                cargando = vm.cargando,
                hayMas = vm.hayMas,
                onDenunciaClick = { vm.abrirDetalle(it, desde = Pantalla.Lista) },
                onCargarMas = { vm.cargarSiguientes() },
                modifier = contenido
            )

            Pantalla.Nueva -> NuevaDenunciaScreen(
                enviando = vm.enviando,
                onGuardar = { dni, tipo, direccion, descripcion ->
                    vm.registrar(dni, tipo, direccion, descripcion)
                },
                onCancelar = { vm.volver() },
                modifier = contenido
            )

            Pantalla.Detalle -> {
                val denuncia = vm.seleccionada
                if (denuncia != null) {
                    DetalleScreen(
                        denuncia = denuncia,
                        onAtender = { vm.atender() },
                        modifier = contenido
                    )
                }
            }

            Pantalla.Buscar -> BuscarScreen(
                buscando = vm.buscando,
                resultados = vm.resultadosBusqueda,
                onBuscar = { vm.buscarPorDni(it) },
                onDenunciaClick = { vm.abrirDetalle(it, desde = Pantalla.Buscar) },
                modifier = contenido
            )
        }
    }
}
