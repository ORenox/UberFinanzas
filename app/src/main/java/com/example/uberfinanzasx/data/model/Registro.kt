package com.example.uberfinanzasx.data.model


data class Registro(
    val tipo: String,
    val descripcion: String,
    val monto: Double,
    val comentario: String? = null
)