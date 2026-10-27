package com.mpazpro3.app.ui.screen.estudiante

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mpazpro3.app.navigation.Rutas
import com.mpazpro3.app.ui.components.BarraSuperior
import com.mpazpro3.app.viewmodel.MpazProViewModel

/**
 * Módulo "Experiencia del estudiante" — pantalla principal del estudiante.
 */
@Composable
fun InicioEstudianteScreen(
    navController: NavHostController,
    viewModel: MpazProViewModel,
    onCerrarSesion: () -> Unit
) {
    val usuario by viewModel.usuarioActivo.collectAsState()
    val todos by viewModel.contenidos.collectAsState()
    val misContenidos = viewModel.contenidosDelEstudiante(usuario)
    val estudianteId = usuario?.id.orEmpty()

    Scaffold(topBar = { BarraSuperior("Mis actividades", navController, onCerrarSesion) }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("¡Hola! Elige una asignatura para comenzar.", style = MaterialTheme.typography.titleLarge)
            Text(
                "Contenidos disponibles: ${misContenidos.size} de ${todos.size}",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(misContenidos) { contenido ->
                    val avance = viewModel.progreso(estudianteId, contenido.id)
                    val porcentaje = (avance * 100).toInt() // CÁLCULO DEL PORCENTAJE

                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(contenido.asignatura.etiqueta, style = MaterialTheme.typography.headlineSmall)

                            // FILA NUEVA PARA MOSTRAR LA UNIDAD Y EL PORCENTAJE
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(contenido.unidad, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
                                Text("Avance: $porcentaje%", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                            }

                            LinearProgressIndicator(progress = { avance }, modifier = Modifier.fillMaxWidth())

                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                onClick = {
                                    viewModel.seleccionarContenido(contenido.id)
                                    navController.navigate(Rutas.ESTUDIANTE_MATERIAL)
                                }
                            ) {
                                // CAMBIO DE TEXTO SI YA ESTÁ COMPLETADO
                                Text(if (porcentaje == 100) "Repasar" else "Comenzar", style = MaterialTheme.typography.titleMedium)
                            }
                        }
                    }
                }
            }
        }
    }
}