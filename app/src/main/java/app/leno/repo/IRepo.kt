package app.leno.repo

import app.leno.data.Resource
import app.leno.model.DataNote
import kotlinx.coroutines.flow.Flow

interface IRepo {

    suspend fun getUserData(): Flow<Resource<MutableList<DataNote>>>
}