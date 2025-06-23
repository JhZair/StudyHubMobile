package com.studyhubmobile.models

import kotlinx.serialization.Serializable

@Serializable
data class Question(
    val id: Int,
    val enunciado: String,
    val opciones: List<String>,
    val respuestaCorrecta: String
)
