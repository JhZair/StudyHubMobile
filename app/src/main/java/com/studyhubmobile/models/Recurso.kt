package com.studyhubmobile.models

import kotlinx.serialization.Serializable

@Serializable
data class Recurso(
    val idRecurso: Int,
    val titulo: String,
    val descripcion: String,
    val tipo: String,
    val archivo: String,
    val fechaPublicacion: String,
    val idCurso: Int,
    val idUsuario: Int
)

