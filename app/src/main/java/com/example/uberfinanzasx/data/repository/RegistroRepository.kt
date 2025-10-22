package com.example.uberfinanzasx.data.repository

import com.example.uberfinanzasx.data.network.ApiService

//esto sirve para centralizar la logica del envio

class RegistroRepository {
    fun guardarRegistro(
        tipo: String,
        descripcion: String,
        monto: Double,
        comentario: String?,
        callback: (Boolean, String) -> Unit
    ) {

        ApiService.enviarRegistro(tipo, descripcion, monto, comentario, callback)
    }

    fun obtenerRegistrosDia(callback: (Boolean, List<List<String>>) -> Unit) {
        ApiService.obtenerRegistrosDia(callback)
    }
}














