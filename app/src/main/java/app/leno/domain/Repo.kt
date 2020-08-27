package app.leno.domain

import android.util.Log
import androidx.constraintlayout.widget.Constraints.TAG
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.leno.notes.DataNote
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private var firebaseUserID: String = ""
private lateinit var auth: FirebaseAuth

class Repo {


    fun getUserData(): LiveData<MutableList<DataNote>> {

        val mutableData = MutableLiveData<MutableList<DataNote>>()
        auth = FirebaseAuth.getInstance()
        firebaseUserID = auth.currentUser!!.uid
        FirebaseFirestore.getInstance().collection("UsersNotes").document(firebaseUserID)
            .collection("Notes and Folders")
            .get().addOnSuccessListener { result ->
                val listData = mutableListOf<DataNote>()
                for (document in result) {
                    val title: String? = document.getString("title")
                    val date: String? = document.getString("date")
                    val type: Long? = document.getLong("type")
                    val usuario = DataNote("DEFAULT_URL", title!!, date!!, type)
                    listData.add(usuario)
                }

                mutableData.value = listData
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }

        return mutableData
    }

}