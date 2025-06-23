package com.studyhubmobile.models

import kotlinx.serialization.Serializable

@Serializable
data class Exam(
    val title: String,
    val questions: List<Question>,
    val duration: Int // en minutos
)
