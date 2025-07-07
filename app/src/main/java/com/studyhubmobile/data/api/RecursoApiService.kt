package com.studyhubmobile.data.api
import com.studyhubmobile.models.Recurso
import retrofit2.http.GET
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import com.studyhubmobile.models.LoginRequest
import com.studyhubmobile.models.LoginResponse

interface RecursoApiService {
    @GET("api/recursos")
    suspend fun getRecursos(): List<Recurso>
}


