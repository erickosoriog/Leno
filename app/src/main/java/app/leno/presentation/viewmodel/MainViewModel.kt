package app.leno.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import app.leno.data.vo.Resource
import app.leno.domain.repo.IUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect

class MainViewModel(private val useCase: IUseCase) : ViewModel() {

    val fetchUserData = liveData(Dispatchers.IO) {

        try {

            useCase.userData().collect {
                emit(it)

            }

        } catch (e: Exception) {
            emit(Resource.Failure(e))
            Log.e("ERROR:", e.toString())
        }
    }
}