package com.studyhubmobile.ui.theme.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import com.studyhubmobile.ui.theme.screens.ExamTopBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.studyhubmobile.models.Question
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import android.app.AlertDialog
import kotlinx.coroutines.delay
import androidx.compose.runtime.LaunchedEffect
import com.studyhubmobile.session.SessionManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamScreen(
    navController: NavController,
    route: String
) {
    val context = LocalContext.current
    val courseName = route.substringAfter("exam/")
    // Formatear el nombre del archivo para que coincida exactamente con los nombres de los archivos
    val fileName = when (courseName) {
        "matematicasi" -> "matematicasi"
        "programacion-de-videojuegos" -> "programaciondevideojuegos"
        "metodologia-del-estudio" -> "metodologiadelestudio"
        "introduccion-a-la-vida-universitaria" -> "introduccionalavidauniversitaria"
        "estructuras-discretas-i" -> "estructurasdiscretasi"
        "comunicacion" -> "comunicacion"
        "matenaticaii" -> "matenaticaii"
        "estructurasdiscretasii" -> "estructurasdiscretasii"
        "introduccionalacienciadelacomputacion" -> "introduccionalacienciadelacomputacion"
        "cienciadelacomputacioni" -> "cienciadelacomputacioni"
        "introduccionalafilosofia" -> "introduccionalafilosofia"
        "personamatrimonioyfamilia" -> "personamatrimonioyfamilia"
        else -> courseName
    }

    // Cargar preguntas desde el archivo JSON
    val questions = remember(courseName) {
        try {
            val jsonString = context.assets.open("questions/$fileName.json").bufferedReader().use { reader ->
                reader.readText()
            }
            Json.decodeFromString<List<Question>>(jsonString)
        } catch (e: Exception) {
            println("Error al cargar preguntas: ${e.message}")
            e.printStackTrace()

            // Mostrar diálogo de error
            AlertDialog.Builder(context)
                .setTitle("Error")
                .setMessage("No se pudieron cargar las preguntas para el curso ${courseName}.\n" +
                        "Verifica que el archivo existe en la carpeta assets/questions/\n" +
                        "Nombre buscado: $fileName.json")
                .setPositiveButton("OK") { _, _ ->
                    navController.popBackStack("simulacro", false)
                }
                .show()

            emptyList()
        }
    }

    // Si no hay preguntas, mostrar mensaje y salir
    if (questions.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No se encontraron preguntas para este curso",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { navController.popBackStack("simulacro", false) }
                ) {
                    Text("Volver")
                }
            }
        }
        return
    }

    // Estado del examen
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var selectedAnswers by remember(questions) {
        mutableStateOf(List(questions.size) { -1 })
    }
    var timeRemaining by remember { mutableStateOf(30 * 60) } // 30 minutos
    var isExamComplete by remember { mutableStateOf(false) }
    var showResults by remember { mutableStateOf(false) }
    var lastScore by remember { mutableStateOf<Pair<Int, Int>?>(null) } // (correctas, total)

    // Actualizar estadísticas solo una vez al mostrar resultados
    LaunchedEffect(showResults) {
        if (showResults && lastScore == null) {
            // Depuración: imprimir selectedAnswers y respuestas correctas
            println("selectedAnswers: $selectedAnswers")
            println("respuestasCorrectas: ${questions.map { it.respuestaCorrecta }}")
            val correctAnswers = questions.mapIndexed { idx, question ->
                val seleccion = selectedAnswers.getOrNull(idx)
                if (seleccion != null && seleccion >= 0 && seleccion < question.opciones.size) {
                    val respuestaSeleccionada = question.opciones[seleccion]
                    val correcta = question.respuestaCorrecta
                    println("Pregunta $idx: seleccion=$respuestaSeleccionada, correcta=$correcta")
                    respuestaSeleccionada == correcta
                } else {
                    false
                }
            }.count { it }
            val totalQuestions = questions.size
            lastScore = Pair(correctAnswers, totalQuestions)
            // Actualizar estadísticas
            val prevStats = SessionManager.userStats
            val newStats = prevStats.copy(
                correctAnswers = prevStats.correctAnswers + correctAnswers,
                totalQuestions = prevStats.totalQuestions + totalQuestions,
                totalExams = prevStats.totalExams + 1,
                averageScore = if (prevStats.totalExams + 1 > 0) ((prevStats.averageScore * prevStats.totalExams + (correctAnswers.toFloat() / totalQuestions)) / (prevStats.totalExams + 1)) else 0f
            )
            SessionManager.userStats = newStats
        }
        if (!showResults) {
            lastScore = null
        }
    }

    // Cronómetro
    LaunchedEffect(Unit) {
        while (timeRemaining > 0 && !isExamComplete) {
            delay(1000)
            timeRemaining--
        }
        if (timeRemaining <= 0) {
            isExamComplete = true
            navController.popBackStack("simulacro", false)
        }
    }

    // Estructura de la pantalla
    Scaffold(
        topBar = {
            ExamTopBar(
                title = courseName,
                onBackClick = { navController.popBackStack("simulacro", false) }
            )
        },
        modifier = Modifier.background(Color(0xFF0f172a)),
        contentColor = Color.White
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Pregunta ${currentQuestionIndex + 1} de ${questions.size}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = questions[currentQuestionIndex].enunciado,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Justify
                        )
                    }
                }
            }

            // Opciones de respuesta
            if (questions.isNotEmpty() && currentQuestionIndex < questions.size) {
                items(questions[currentQuestionIndex].opciones) { option ->
                    val isSelected = selectedAnswers[currentQuestionIndex] == questions[currentQuestionIndex].opciones.indexOf(option)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clickable {
                                selectedAnswers = selectedAnswers.toMutableList().apply {
                                    set(currentQuestionIndex, questions[currentQuestionIndex].opciones.indexOf(option))
                                }
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = null,
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = MaterialTheme.colorScheme.primary
                                )
                            )
                            Text(
                                text = option,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            // Botones de navegación
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (currentQuestionIndex > 0) {
                        Button(
                            onClick = { currentQuestionIndex-- },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Anterior")
                        }
                    }
                    if (currentQuestionIndex < questions.size - 1) {
                        Button(
                            onClick = { currentQuestionIndex++ },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Siguiente")
                        }
                    } else {
                        Button(
                            onClick = {
                                isExamComplete = true
                                showResults = true
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Terminar Examen")
                        }
                    }
                }
            }

            // Cronómetro
            item {
                Text(
                    text = "Tiempo restante: ${timeRemaining / 60}:${String.format("%02d", timeRemaining % 60)}",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Mostrar resultados si el examen está completo
            if (showResults) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "Resultados del Examen",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary
                            )

                            // Mostrar puntaje calculado
                            val score = lastScore ?: Pair(0, questions.size)
                            Text(
                                text = "Preguntas correctas: ${score.first} de ${score.second}",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Button(
                                onClick = {
                                    navController.navigate("simulacro")
                                    showResults = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Volver al Simulacro")
                            }
                        }
                    }
                }
            }
        }
    }
}