package com.studyhubmobile.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.studyhubmobile.data.api.RecursoApiService
import com.studyhubmobile.data.api.AuthApiService
import com.studyhubmobile.models.LoginRequest
import com.studyhubmobile.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import com.studyhubmobile.models.LoginResponse
import com.studyhubmobile.data.api.RecursoNetworkModule

//AuthRetrofitClient
object RecursoNetworkModule {
    // Reemplaza esto con la URL base de tu API
    private const val BASE_URL = "https://studyhubbackend-vdyi.onrender.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Este es el servicio que usarás en la pantalla
    val apiService = retrofit.create(RecursoApiService::class.java)
}

