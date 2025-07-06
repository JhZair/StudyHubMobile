package com.studyhubmobile.ui.theme.screens

import android.text.Html
import android.text.Spanned
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.coroutines.coroutineContext
import kotlin.random.Random

// Data classes
@kotlinx.serialization.Serializable
data class TriviaQuestion(
    val id: Int,
    val enunciado: String,
    val opciones: List<String>,
    val respuestaCorrecta: String
)

@kotlinx.serialization.Serializable
data class TriviaResponse(
    val results: List<TriviaResult>
)

@kotlinx.serialization.Serializable
data class TriviaResult(
    val question: String,
    val correct_answer: String,
    val incorrect_answers: List<String>
)

// Helper functions
private fun decodeHtmlEntities(text: String): String {
    val spanned: Spanned = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
    return spanned.toString()
}

private fun <T> List<T>.randomShuffle(): List<T> = this.shuffled(Random.Default)

private fun createTriviaQuestions(results: List<TriviaResult>): List<TriviaQuestion> =
    results.mapIndexed { index, item ->
        val question = decodeHtmlEntities(item.question)
        val correctAnswer = decodeHtmlEntities(item.correct_answer)
        val incorrectAnswers = item.incorrect_answers.map { decodeHtmlEntities(it) }
        
        TriviaQuestion(
            id = index + 1,
            enunciado = question,
            opciones = (incorrectAnswers + correctAnswer).randomShuffle(),
            respuestaCorrecta = correctAnswer
        )
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TriviaExamScreen(navController: NavController) {
    var preguntasState by remember { mutableStateOf<List<TriviaQuestion>>(emptyList()) }
    var respuestasState by remember { mutableStateOf<Map<Int, String>>(emptyMap()) }
    var finalizado by remember { mutableStateOf(false) }
    var puntaje by remember { mutableStateOf(0) }

    // Inicializar preguntas al cargar la pantalla
    LaunchedEffect(Unit) {
        fetchTriviaQuestions(
            onSuccess = { questions ->
                preguntasState = questions
            },
            onError = { error ->
                Log.e("TriviaExam", "Error fetching trivia questions: $error")
            }
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        topBar = {
            TopAppBar(
                title = { Text("Examen de Trivia") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Regresar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0f172a),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(preguntasState) { pregunta ->
                PreguntaCard(
                    pregunta = pregunta,
                    respuestaSeleccionada = respuestasState[pregunta.id],
                    finalizado = finalizado,
                    onRespuesta = { respuesta ->
                        if (!finalizado) {
                            respuestasState = respuestasState + (pregunta.id to respuesta)
                        }
                    }
                )
            }

            if (preguntasState.isNotEmpty()) {
                item {
                    if (!finalizado) {
                        Button(
                            onClick = { 
                                finalizarExamen(
                                    preguntas = preguntasState,
                                    respuestas = respuestasState,
                                    onPuntaje = { puntos ->
                                        puntaje = puntos
                                        finalizado = true
                                    }
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF0f172a),
                                contentColor = Color.White
                            )
                        ) {
                            Text("Finalizar Trivia")
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .background(
                                    color = when {
                                        puntaje == preguntasState.size -> Color(0xFF10B981) // Verde para 100%
                                        puntaje >= (preguntasState.size * 0.7) -> Color(0xFF3B82F6) // Azul para 70% o más
                                        else -> Color(0xFF1e293b) // Azul oscuro para menos del 70%
                                    },
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "Resultado Final",
                                style = MaterialTheme.typography.headlineMedium,
                                color = Color.White
                            )
                            Text(
                                text = "Tu puntaje: $puntaje de ${preguntasState.size}",
                                style = MaterialTheme.typography.headlineLarge,
                                color = Color.White
                            )
                            Text(
                                text = "Porcentaje: ${(puntaje.toFloat() / preguntasState.size * 100).toInt()}%",
                                style = MaterialTheme.typography.headlineMedium,
                                color = Color.White
                            )
                            
                            // Botón para volver a intentar
                            Button(
                                onClick = {
                                    // Reiniciar el examen
                                    respuestasState = emptyMap()
                                    finalizado = false
                                    puntaje = 0
                                    fetchTriviaQuestions(
                                        onSuccess = { questions ->
                                            preguntasState = questions
                                        },
                                        onError = { error ->
                                            Log.e("TriviaExam", "Error fetching trivia questions: $error")
                                        }
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF0f172a),
                                    contentColor = Color.White
                                )
                            ) {
                                Text("Volver a Intentar")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PreguntaCard(
    pregunta: TriviaQuestion,
    respuestaSeleccionada: String?,
    finalizado: Boolean,
    onRespuesta: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0f172a)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = pregunta.enunciado,
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            pregunta.opciones.forEach { opcion ->
                OptionButton(
                    opcion = opcion,
                    isSelected = respuestaSeleccionada == opcion,
                    isCorrect = finalizado && opcion == pregunta.respuestaCorrecta,
                    isIncorrect = finalizado && opcion == respuestaSeleccionada && opcion != pregunta.respuestaCorrecta,
                    onClick = { onRespuesta(opcion) },
                    disabled = finalizado
                )
            }

            if (finalizado) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        if (respuestaSeleccionada == pregunta.respuestaCorrecta) 
                            Icons.Default.Check
                        else 
                            Icons.Default.Close,
                        contentDescription = if (respuestaSeleccionada == pregunta.respuestaCorrecta) "Correcta" else "Incorrecta",
                        tint = if (respuestaSeleccionada == pregunta.respuestaCorrecta) Color.Green else Color.Red
                    )
                    Text(
                        text = if (respuestaSeleccionada == pregunta.respuestaCorrecta) 
                            "Correcta"
                        else 
                            "Incorrecta. Respuesta correcta: ${pregunta.respuestaCorrecta}",
                        color = if (respuestaSeleccionada == pregunta.respuestaCorrecta) Color.Green else Color.Red,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun OptionButton(
    opcion: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    isIncorrect: Boolean,
    onClick: () -> Unit,
    disabled: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(
                color = when {
                    isCorrect -> Color.Green
                    isIncorrect -> Color.Red
                    isSelected -> Color(0xFF0f172a)
                    else -> Color(0xFF1e293b)
                }
            )
            .then(
                if (!disabled) {
                    Modifier.clickable(
                        onClick = onClick
                    )
                } else {
                    Modifier
                }
            )
            .padding(16.dp)
    ) {
        Text(
            text = opcion,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White
        )
    }
}

private fun decodeHTMLEntities(text: String): String {
    return text
        .replace("&quot;", "\"")
        .replace("&apos;", "'")
        .replace("&amp;", "&")
        .replace("&lt;", "<")
        .replace("&gt;", ">")
}

private fun fetchTriviaQuestions(
    onSuccess: (List<TriviaQuestion>) -> Unit,
    onError: (String) -> Unit
) {
    val client = OkHttpClient()
    val request = Request.Builder()
        .url("https://opentdb.com/api.php?amount=5&type=multiple")
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            onError(e.message ?: "Error desconocido")
        }

        override fun onResponse(call: Call, response: Response) {
            response.use { resp ->
                val body = resp.body?.string() ?: return onError("Respuesta vacía")
                try {
                    val gson = Gson()
                    val type = object : TypeToken<TriviaResponse>() {}.type
                    val triviaResponse = gson.fromJson<TriviaResponse>(body, type)
                    
                    val preguntas = createTriviaQuestions(triviaResponse.results)
                    onSuccess(preguntas)
                } catch (e: Exception) {
                    onError("Error parsing trivia response: ${e.message}")
                }
            }
        }
    })
}

private fun finalizarExamen(
    preguntas: List<TriviaQuestion>,
    respuestas: Map<Int, String>,
    onPuntaje: (Int) -> Unit
) {
    var puntos = 0
    preguntas.forEach { pregunta ->
        val respuestaUsuario = respuestas[pregunta.id]
        if (respuestaUsuario != null && respuestaUsuario == pregunta.respuestaCorrecta) {
            puntos++
        }
    }
    onPuntaje(puntos)
}
