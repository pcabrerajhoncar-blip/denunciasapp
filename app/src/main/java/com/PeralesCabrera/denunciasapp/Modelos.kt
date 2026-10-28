package com.PeralesCabrera.denunciasapp

/** Denuncia tal como la devuelve la API. estado: 1 = pendiente, 0 = atendida. */
data class Denuncia(
    val id: Int,
    val dni: String,
    val fecha: String,
    val tipo: String,
    val direccion: String,
    val descripcion: String,
    val estado: Int
)

/** Cuerpo del POST denuncias/ (el estado lo define la API: inicia en 1). */
data class DenunciaRequest(
    val dni: String,
    val fecha: String,
    val tipo: String,
    val direccion: String,
    val descripcion: String
)
