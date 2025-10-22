package com.example.uberfinanzasx.ui.components


import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RegistroItem(
    titulo:String,
    tituloBoton:String,
    opcionesDescripcion:List<String>,
    cardColor: Color, // 🔹 nuevo parámetro de color
    onGuardar: (Double,String,String?)->Unit
){
    var valor by remember{ mutableStateOf("") }
    var descripcionSeleccionada by remember { mutableStateOf<String?>(null) }
    var comentario by remember { mutableStateOf("") }
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        )
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        ) {
            Text(titulo, style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = valor,
                onValueChange = { valor = it },
                label = { Text("Valor") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )
            //campo para ingresar las etiquetas

            Text("Tipo",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(top = 12.dp, bottom = 4.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
                opcionesDescripcion.forEach { opcion->
                    FilterChip(
                        selected = descripcionSeleccionada == opcion,
                        onClick = {descripcionSeleccionada = opcion},
                        label = {Text(opcion)},
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                    )
                }
            }

            //campo para ingresar un comentario
            OutlinedTextField(
                value = comentario,
                onValueChange = { comentario = it },
                label = { Text("Comentario (Opcional)") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    when {
                        valor.isEmpty() -> {
                            // podrías mostrar un mensaje visual si quieres
                            println("⚠️ Debes ingresar un valor")
                            Toast.makeText(context,"ingresa un valor",Toast.LENGTH_SHORT).show()
                        }
                        descripcionSeleccionada == null -> {
                            println("⚠️ Debes seleccionar un tipo")
                            Toast.makeText(context,"Selecciona un tipo",Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            onGuardar(
                                valor.toDouble(),
                                descripcionSeleccionada!!,
                                comentario.ifEmpty { null }
                            )
                           Toast.makeText(context,"Guardando...",Toast.LENGTH_SHORT).show()

                        }
                    }
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(tituloBoton)
            }

        }

    }



}

@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Vista previa de RegistroItem"
)
@Composable
fun PreviewRegistroItem() {
    MaterialTheme {
        RegistroItem(
            titulo = "Registrar Ingreso",
            tituloBoton = "Guardar",
            opcionesDescripcion = listOf("Salario", "Venta", "Regalo", "Otro"),
            cardColor = Color(0xFFFFE0E0),
            onGuardar = { valor, descripcion, comentario ->
                // Ejemplo: solo imprime los valores en consola (no se usa en preview real)
                println("Valor: $valor, Descripción: $descripcion, Comentario: $comentario")
            }
        )
    }
}
