package com.studyhubmobile.models

data class UserStats(
    val totalExams: Int = 0,
    val correctAnswers: Int = 0,
    val totalQuestions: Int = 0,
    val averageScore: Float = 0f
)

data class UserProfile(
    val name: String,
    val email: String,
    val currentSemester: Int,
    val stats: UserStats = UserStats()
)

// Modelo para el cuerpo de la solicitud de login que envías a la API
data class LoginRequest(
    val email: String,
    val password: String // Coincide con lo que espera tu backend (tu SQL usa 'password')
)

// Modelo para el objeto 'usuario' que recibes en la respuesta del login
data class User(
    val id_usuario: Int,
    val nombre: String,
    val email: String,
    val fecha_registro: String?,
    val ultimo_acceso: String?,
    val universidad: String?
)

// Modelo para la respuesta completa del login que recibes de la API
data class LoginResponse(
    val mensaje: String?,
    val usuario: User?
)

// Modelo para la respuesta del logout (si decides implementarlo)
data class LogoutResponse(
    val mensaje: String?
)

fun UserProfile.copy(
    name: String = this.name,
    email: String = this.email,
    currentSemester: Int = this.currentSemester,
    stats: UserStats = this.stats
) = UserProfile(name, email, currentSemester, stats)



