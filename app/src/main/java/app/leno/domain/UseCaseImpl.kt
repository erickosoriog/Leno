package app.leno.domain

import app.leno.data.Resource
import app.leno.data.model.ModelData
import app.leno.data.repo.IRepo
import kotlinx.coroutines.flow.Flow

class UseCaseImpl(private val iRepo: IRepo) : IUseCase {

    override suspend fun userData(): Flow<Resource<MutableList<ModelData>>> = iRepo.getUserData()
}