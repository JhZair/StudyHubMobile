package com.studyhubmobile.data.api

import com.studyhubmobile.models.Recurso
import retrofit2.http.*
import retrofit2.Response

interface RecursoApiService {
    @GET("api/recursos")
    suspend fun getRecursos(): List<Recurso>

    @GET("api/recursos/{id}")
    suspend fun getRecurso(@Path("id") id: Int): Recurso

    @POST("api/recursos")
    suspend fun createRecurso(@Body recurso: Recurso): Recurso

    @PUT("api/recursos/{id}")
    suspend fun updateRecurso(@Path("id") id: Int, @Body recurso: Recurso): Recurso

    @DELETE("api/recursos/{id}")
    suspend fun deleteRecurso(@Path("id") id: Int): Response<Unit>
}


