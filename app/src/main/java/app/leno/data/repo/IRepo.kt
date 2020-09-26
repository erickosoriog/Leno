package app.leno.data.repo

import app.leno.data.Resource
import app.leno.data.model.ModelData
import kotlinx.coroutines.flow.Flow

interface IRepo {

    suspend fun getUserData(): Flow<Resource<MutableList<ModelData>>>
}