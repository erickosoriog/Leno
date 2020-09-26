package app.leno.data.repo

import android.content.ContentValues.TAG
import android.util.Log
import app.leno.data.Resource
import app.leno.data.model.ModelData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.Query
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class RepoImpl : IRepo {

    private var firebaseUserID: String? = null
    private lateinit var auth: FirebaseAuth

    @ExperimentalCoroutinesApi
    override suspend fun getUserData(): Flow<Resource<MutableList<ModelData>>> = callbackFlow {

        auth = FirebaseAuth.getInstance()
        firebaseUserID = auth.currentUser!!.uid

        val listData = mutableListOf<ModelData>()

        val fireStoreDB = FirebaseFirestore
            .getInstance()
            .collection("UsersNotes")
            .document(firebaseUserID!!)
            .collection("DataRepo and Folders")
            .orderBy("created", Query.Direction.DESCENDING)

        val listenerData: ListenerRegistration =
            fireStoreDB.addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, error ->

                if (error != null) {
                    channel.close(error.cause)
                    Log.w(TAG, "Listen failed.", error)
                    return@addSnapshotListener
                }

                val source = if (snapshot != null && snapshot.metadata.hasPendingWrites())
                    "Local"
                else
                    "Server"

                if (snapshot != null) {

                    for (document in snapshot) {
                        val documentID = document.toObject(ModelData::class.java)
                        documentID.id = document.id
                        listData.add(documentID)
                        Log.d(TAG, "documentID: ${document.id}")
                        channel.offer(Resource.Success(listData))
                    }

                    channel.offer(Resource.Success(snapshot.toObjects(ModelData::class.java)))
                    Log.d(TAG, "Current data in DataRepo and Folders: $listData")

                } else {

                    channel.close()
                    Log.d(TAG, "$source data: null")
                }
            }

        awaitClose {
            listenerData.remove()
        }

    }
}

