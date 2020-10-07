package app.leno.domain.repo

import app.leno.data.model.ModelData
import app.leno.data.vo.Resource
import kotlinx.coroutines.flow.Flow

interface IUseCase {

    suspend fun userData(): Flow<Resource<MutableList<ModelData>>>
}