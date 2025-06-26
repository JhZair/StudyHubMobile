package com.studyhubmobile.data.api
import com.studyhubmobile.models.Recurso
import retrofit2.http.GET
import kotlinx.coroutines.flow.Flow

interface RecursoApiService {
    @GET("api/recursos")
    suspend fun getRecursos(): List<Recurso>
}