package app.leno.Viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.leno.domain.Repo
import app.leno.notes.DataNote

class MainViewModel : ViewModel() {

    private val repo = Repo()

    fun fetchUserData(): LiveData<MutableList<DataNote>> {
        val mutableData = MutableLiveData<MutableList<DataNote>>()
        repo.getUserData().observeForever { UserList ->
            mutableData.value = UserList
        }

        return mutableData
    }
}