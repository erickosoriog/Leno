package app.leno.data.network.repo

import app.leno.data.model.ModelData
import app.leno.data.vo.Resource
import kotlinx.coroutines.flow.Flow

interface IRepo {

    suspend fun getUserData(): Flow<Resource<MutableList<ModelData>>>
}