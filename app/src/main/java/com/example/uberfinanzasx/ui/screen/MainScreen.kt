package com.example.uberfinanzasx.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.uberfinanzasx.ui.components.RegistroItem
import com.example.uberfinanzasx.data.model.Registro
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import com.example.uberfinanzasx.data.repository.RegistroRepository
import com.example.uberfinanzasx.ui.components.DataTable
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {



    val repo = RegistroRepository()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope() // ✅ scope para lanzar corrutinas

    //variables para la tabla
    var registros by remember { mutableStateOf<List<List<String>>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    Scaffold (
        topBar = {
            TopAppBar(
                title= { Text(" UBER FINANZAS X")},
                colors =TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2E2F2F),
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        // 👇 Recordatorio del estado de scroll
        val scrollState = rememberScrollState()


        Column(
        modifier = Modifier.fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(scrollState)
        )
    {
            RegistroItem("Ingresos",
                "Guardar Ingresos",
                listOf("UBER", "Otro"),
                Color(0xFF4A6D86),

                onGuardar = { valor: Double, descripcion: String, comentario: String? ->
                    repo.guardarRegistro(
                        "ingreso",
                        descripcion,
                        valor,
                        comentario
                    ) { exito, mensaje ->
                        coroutineScope.launch { // ✅ mostrar snackbar en el hilo principal
                            val text = if (exito) "✅ Guardado correctamente" else "❌ Error: $mensaje"
                            snackbarHostState.showSnackbar(text)
                        }
                    }
                }
            )

        RegistroItem("Egresos",
            "Guardar Egresos",
            listOf("Gasolina", "Mecanica","Otros"),
            Color(0xFF879375),

            onGuardar = { valor: Double, descripcion: String, comentario: String? ->
                repo.guardarRegistro(
                    "egreso",
                    descripcion,
                    valor,
                    comentario
                ) { exito, mensaje ->
                    if(exito) coroutineScope.launch {
                        val text = if (exito) "✅ Guardado correctamente" else "❌ Error: $mensaje"
                        snackbarHostState.showSnackbar(text)

                    }


                }

            }

        )
        //Tabla de registers del día
        LaunchedEffect(Unit) {
            repo.obtenerRegistrosDia { success, data ->
                registros = data
                isLoading = false
            }
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            DataTable(
                headers = listOf("Fecha", "Tipo", "Descripción", "Monto", "Comentario"),
                data = registros
            )
        }


        }
    }


}











