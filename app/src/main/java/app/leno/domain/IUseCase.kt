package app.leno.domain

import app.leno.data.Resource
import app.leno.model.DataNote
import kotlinx.coroutines.flow.Flow

interface IUseCase {

    suspend fun userData(): Flow<Resource<MutableList<DataNote>>>
}