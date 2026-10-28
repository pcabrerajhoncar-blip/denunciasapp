package com.PeralesCabrera.denunciasapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

sealed interface Pantalla {
    data object Lista : Pantalla
    data object Nueva : Pantalla
    data object Detalle : Pantalla
    data object Buscar : Pantalla
}

class DenunciasViewModel : ViewModel() {

    private val api = RetrofitClient.api
    private val tamanoPagina = 5
    private var pantallaAnterior: Pantalla = Pantalla.Lista

    var pantalla by mutableStateOf<Pantalla>(Pantalla.Lista)
        private set

    // ---- Lista paginada ----
    var denuncias by mutableStateOf<List<Denuncia>>(emptyList())
        private set
    var hayMas by mutableStateOf(true)
        private set
    var cargando by mutableStateOf(false)
        private set

    // ---- Registro ----
    var enviando by mutableStateOf(false)
        private set

    // ---- Detalle ----
    var seleccionada by mutableStateOf<Denuncia?>(null)
        private set

    // ---- Búsqueda por DNI (null = todavía no se ha buscado) ----
    var resultadosBusqueda by mutableStateOf<List<Denuncia>?>(null)
        private set
    var buscando by mutableStateOf(false)
        private set

    // ---- Mensajes para el usuario (Snackbar) ----
    var mensaje by mutableStateOf<String?>(null)
        private set

    init {
        cargarSiguientes()
    }

    fun limpiarMensaje() {
        mensaje = null
    }

    // ---------- Navegación ----------
    fun irA(destino: Pantalla) {
        if (destino == Pantalla.Buscar) resultadosBusqueda = null
        pantalla = destino
    }

    fun volver() {
        pantalla = if (pantalla == Pantalla.Detalle) pantallaAnterior else Pantalla.Lista
    }

    // ---------- GET denuncias/?offset=&limit= ----------
    fun cargarSiguientes() {
        if (cargando || !hayMas) return
        viewModelScope.launch {
            cargando = true
            try {
                val nuevas = api.listar(offset = denuncias.size, limit = tamanoPagina)
                denuncias = denuncias + nuevas
                hayMas = nuevas.size == tamanoPagina
            } catch (e: Exception) {
                mensaje = "No se pudo cargar la lista: ${e.message}"
            } finally {
                cargando = false
            }
        }
    }

    fun recargar() {
        denuncias = emptyList()
        hayMas = true
        cargarSiguientes()
    }

    // ---------- POST denuncias/ ----------
    fun registrar(dni: String, tipo: String, direccion: String, descripcion: String) {
        if (enviando) return
        viewModelScope.launch {
            enviando = true
            try {
                val fecha = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
                api.registrar(DenunciaRequest(dni, fecha, tipo, direccion, descripcion))
                mensaje = "Denuncia registrada"
                pantalla = Pantalla.Lista
                recargar()
            } catch (e: Exception) {
                mensaje = "No se pudo registrar: ${e.message}"
            } finally {
                enviando = false
            }
        }
    }

    // ---------- GET denuncias/{id} ----------
    fun abrirDetalle(denuncia: Denuncia, desde: Pantalla) {
        seleccionada = denuncia
        pantallaAnterior = desde
        pantalla = Pantalla.Detalle
        viewModelScope.launch {
            try {
                seleccionada = api.obtener(denuncia.id)
            } catch (e: Exception) {
                mensaje = "No se pudo actualizar el detalle: ${e.message}"
            }
        }
    }

    // ---------- PUT denuncias/{id} ----------
    fun atender() {
        val actual = seleccionada ?: return
        viewModelScope.launch {
            try {
                val atendida = api.atender(actual.id)
                seleccionada = atendida
                denuncias = denuncias.map { if (it.id == atendida.id) atendida else it }
                resultadosBusqueda = resultadosBusqueda?.map { if (it.id == atendida.id) atendida else it }
                mensaje = "Denuncia atendida"
            } catch (e: Exception) {
                mensaje = "No se pudo atender la denuncia: ${e.message}"
            }
        }
    }

    // ---------- GET denuncias/ciudadano/{dni} ----------
    fun buscarPorDni(dni: String) {
        viewModelScope.launch {
            buscando = true
            try {
                resultadosBusqueda = api.porCiudadano(dni)
            } catch (e: Exception) {
                mensaje = "No se pudo buscar: ${e.message}"
            } finally {
                buscando = false
            }
        }
    }
}
