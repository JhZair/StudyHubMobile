package com.studyhubmobile.data.api

import com.studyhubmobile.models.LoginRequest
import com.studyhubmobile.models.LoginResponse
import com.studyhubmobile.models.RegisterResponse
import com.studyhubmobile.models.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>
}
