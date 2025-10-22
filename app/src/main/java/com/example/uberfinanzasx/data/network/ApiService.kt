package com.example.uberfinanzasx.data.network

import android.widget.Toast
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

object ApiService{

    private val client = OkHttpClient()
    private const val BASE_URL = "https://script.google.com/macros/s/AKfycbwM6jy-pln51C4EY_a5-rTju1FCkH5yaaHgTDASoncV42Ti7P9NJDvSNacsw9yv06Af/exec"

    fun enviarRegistro(tipo:String, descripcion:String,monto: Double, comentario:String?,callback:(Boolean,String)-> Unit){

        val json = JSONObject().apply {
            put("tipo",tipo)
            put("descripcion", descripcion)
            put("monto", monto)
            comentario?.let { put("comentario", it) }
        }

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = json.toString().toRequestBody(mediaType)
        val request = Request.Builder()
            .url(BASE_URL)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure( call : Call, e: IOException){
                callback(false, e.message?: "Error desconocido")
            }

            override fun onResponse(call: Call, response: Response) {
                val respuesta = response.body?.string() ?: ""
                callback(response.isSuccessful, respuesta)
            }
        })


    }

}