package com.studyhubmobile.data.repository

import com.studyhubmobile.data.api.RecursoApiService
import com.studyhubmobile.models.Recurso
import retrofit2.Response
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RecursoRepository @Inject constructor(
    private val apiService: RecursoApiService
) {
    suspend fun getRecursos(): Flow<List<Recurso>> = flow {
        try {
            val recursos = apiService.getRecursos()
            emit(recursos)
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getRecurso(id: Int): Flow<Recurso> = flow {
        try {
            val recurso = apiService.getRecurso(id)
            emit(recurso)
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun createRecurso(recurso: Recurso): Flow<Recurso> = flow {
        try {
            val nuevoRecurso = apiService.createRecurso(recurso)
            emit(nuevoRecurso)
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun updateRecurso(id: Int, recurso: Recurso): Flow<Recurso> = flow {
        try {
            val recursoActualizado = apiService.updateRecurso(id, recurso)
            emit(recursoActualizado)
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun deleteRecurso(id: Int): Flow<Response<Unit>> = flow {
        try {
            val response = apiService.deleteRecurso(id)
            emit(response)
        } catch (e: Exception) {
            throw e
        }
    }
}
