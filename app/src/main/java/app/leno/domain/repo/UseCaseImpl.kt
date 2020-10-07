package app.leno.domain.repo

import app.leno.data.model.ModelData
import app.leno.data.network.repo.IRepo
import app.leno.data.vo.Resource
import kotlinx.coroutines.flow.Flow

class UseCaseImpl(private val iRepo: IRepo) : IUseCase {

    override suspend fun userData(): Flow<Resource<MutableList<ModelData>>> = iRepo.getUserData()
}