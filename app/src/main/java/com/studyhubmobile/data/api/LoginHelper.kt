package com.studyhubmobile.utils

import com.studyhubmobile.data.repository.AuthRepository
import com.studyhubmobile.models.LoginRequest
import com.studyhubmobile.models.User
import kotlinx.coroutines.*
import java.lang.Exception


suspend fun hacerLoginSimple(
    email: String,
    password: String,
    onResult: (User?) -> Unit,
    onError: (String) -> Unit
) {
    try {
        val response = AuthRepository().login(email, password)
        if (response.isSuccessful) {
            val body = response.body()
            if (body?.usuario != null) {
                AuthRepository().currentUser = body.usuario
                onResult(body.usuario)
            } else {
                onError("No se encontró el usuario en la respuesta")
            }
        } else {
            onError("Error del servidor: ${response.code()}")
        }
    } catch (e: Exception) {
        onError("Excepción: ${e.message}")
    }
}
