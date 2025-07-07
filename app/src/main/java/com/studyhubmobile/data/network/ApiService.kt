package com.studyhubmobile.data.network

import com.studyhubmobile.models.RegisterRequest
import com.studyhubmobile.models.RegisterResponse
import com.studyhubmobile.models.Recurso
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @Multipart
    @POST("recursos")
    suspend fun uploadResource(
        @Part("titulo") titulo: RequestBody,
        @Part("descripcion") descripcion: RequestBody,
        @Part("tipo") tipo: RequestBody,
        @Part("idCurso") idCurso: RequestBody,
        @Part archivo: MultipartBody.Part
    ): Response<Recurso>

    @DELETE("recursos/{id}")
    suspend fun deleteResource(@Path("id") id: Int): Response<Void>

    @GET("recursos")
    suspend fun getResources(): Response<List<Recurso>>

    companion object {
        private const val BASE_URL = "https://studyhubbackend-vdyi.onrender.com/api/"
        
        fun create(): ApiService {
            val retrofit = retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .build()
            
            return retrofit.create(ApiService::class.java)
        }
    }
}
