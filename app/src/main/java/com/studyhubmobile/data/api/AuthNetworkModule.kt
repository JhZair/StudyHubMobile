package com.studyhubmobile.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AuthNetworkModule {
    private const val BASE_URL = "https://studyhubbackend-vdyi.onrender.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: AuthApiService = retrofit.create(AuthApiService::class.java)
}
