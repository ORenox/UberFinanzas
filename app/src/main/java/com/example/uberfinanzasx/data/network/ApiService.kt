package com.example.uberfinanzasx.data.network

import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
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
    fun obtenerRegistrosDia(callback: (Boolean, List<List<String>>) -> Unit){

        val url = "https://script.google.com/macros/s/AKfycbwJ5Yh9rddOPQqguDlWzNz1_npjJmOD7k7aE5mODEZ0v_fX6MWsUcXSvhZh7aVD6Q5S/exec?mode=all"
        val request = Request.Builder()
            .url(url)
            .get()
            .build()
        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                callback(false, emptyList())
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                if( !response.isSuccessful || body.isNullOrEmpty()){
                    callback(false, emptyList())
                    return
                }

                try {
                    val json = JSONObject(body)
                    val success = json.getBoolean("success")
                    val dataArray = json.getJSONArray("data")//aqui es donde debo ajustar segun mi json

                    val result = mutableListOf<List<String>>()

                    for (i in 0 until dataArray.length()) {
                        val rowArray = dataArray.getJSONArray(i)
                        val row = mutableListOf<String>()
                        for (j in 0 until rowArray.length()) {
                            row.add(rowArray.getString(j))
                        }
                        result.add(row)
                    }

                    // Eliminamos la primera fila si son encabezados
                    val rowsWithoutHeader = if (result.isNotEmpty()) result.drop(1) else result

                    callback(success, rowsWithoutHeader)
                }catch (e: Exception){
                    e.printStackTrace()
                    callback(false,emptyList())
                }
            }

        })
    }

}






















