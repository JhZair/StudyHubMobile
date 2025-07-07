package com.studyhubmobile.utils

import com.studyhubmobile.data.api.AuthNetworkModule
import com.studyhubmobile.models.LoginRequest
import com.studyhubmobile.models.User
import kotlinx.coroutines.*
import java.lang.Exception
import com.studyhubmobile.session.SessionManager.currentUser

fun hacerLoginSimple(
    email: String,
    password: String,
    onResult: (User?) -> Unit,
    onError: (String) -> Unit
) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = AuthNetworkModule.apiService.login(LoginRequest(email, password))
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.usuario != null) {
                        currentUser = body.usuario
                        onResult(body.usuario)
                    } else {
                        onError("No se encontró el usuario en la respuesta")
                    }
                } else {
                    onError("Error del servidor: ${response.code()}")
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                onError("Excepción: ${e.message}")
            }
        }
    }
}
