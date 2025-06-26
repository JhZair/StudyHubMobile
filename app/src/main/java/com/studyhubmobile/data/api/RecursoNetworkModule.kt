package com.studyhubmobile.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RecursoNetworkModule {
    // Reemplaza esto con la URL base de tu API
    private const val BASE_URL = "https://studyhubbackend-vdyi.onrender.com/ "

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Este es el servicio que usarás en la pantalla
    val apiService = retrofit.create(RecursoApiService::class.java)
}

