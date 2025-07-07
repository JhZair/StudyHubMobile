package com.studyhubmobile.data.network

import com.studyhubmobile.models.LoginRequest
import com.studyhubmobile.models.LoginResponse
import com.studyhubmobile.models.RegisterRequest
import com.studyhubmobile.models.RegisterResponse
import retrofit2.Response
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.Gson
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiServiceInterface {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>
}

class ApiServiceImpl(private val retrofit: ApiServiceInterface) : ApiServiceInterface {
    override suspend fun login(request: LoginRequest): Response<LoginResponse> = 
        retrofit.login(request)

    override suspend fun register(request: RegisterRequest): Response<RegisterResponse> = 
        retrofit.register(request)
}

object ApiService {
    private val retrofit = Retrofit.Builder()
        .baseUrl(NetworkConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val instance: ApiServiceInterface = ApiServiceImpl(retrofit.create(ApiServiceInterface::class.java))
}
