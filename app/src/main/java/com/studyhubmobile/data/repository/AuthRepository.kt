package com.studyhubmobile.data.repository

import com.studyhubmobile.models.LoginRequest
import com.studyhubmobile.models.LoginResponse
import com.studyhubmobile.models.User
import com.studyhubmobile.models.RegisterRequest
import com.studyhubmobile.models.RegisterResponse
import com.studyhubmobile.data.network.ApiService
import com.studyhubmobile.data.network.ApiServiceInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class AuthRepository {
    private val apiService = ApiService.instance
    var currentUser: User? = null
        public set(value) {
            field = value
        }

    suspend fun login(email: String, password: String): Response<LoginResponse> {
        return withContext(Dispatchers.IO) {
            val response = apiService.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                currentUser = response.body()?.usuario
            }
            response
        }
    }

    suspend fun register(nombre: String, email: String, password: String): Response<RegisterResponse> {
        return withContext(Dispatchers.IO) {
            val response = apiService.register(RegisterRequest(nombre, email, password))
            if (response.isSuccessful) {
                currentUser = response.body()?.usuario
            }
            response
        }
    }




}
