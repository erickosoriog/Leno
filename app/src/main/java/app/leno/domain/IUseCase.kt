package app.leno.domain

import app.leno.data.Resource
import app.leno.data.model.ModelData
import kotlinx.coroutines.flow.Flow

interface IUseCase {

    suspend fun userData(): Flow<Resource<MutableList<ModelData>>>
}