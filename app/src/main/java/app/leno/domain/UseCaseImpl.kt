package app.leno.domain

import app.leno.data.Resource
import app.leno.model.DataNote
import app.leno.repo.IRepo
import kotlinx.coroutines.flow.Flow

class UseCaseImpl(private val iRepo: IRepo) : IUseCase {

    override suspend fun userData(): Flow<Resource<MutableList<DataNote>>> = iRepo.getUserData()
}