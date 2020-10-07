package app.leno.data.network.user

import android.content.ContentValues
import android.util.Log
import app.leno.data.model.ModelUser
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class UserRepoInteractImpl : UserRepoInteract {

    private var firebaseUserID: String? = null

    override fun createUserDB(username: String, email: String) {

        firebaseUserID = FirebaseAuth.getInstance().currentUser?.uid
        val docRef = FirebaseFirestore.getInstance().collection("Users")
            .document(firebaseUserID!!)

        val user = ModelUser()
        user.id = firebaseUserID
        user.username = username
        user.email = email
        user.registered = Timestamp.now()

        docRef.get().addOnCompleteListener {
            if (it.isSuccessful) {
                if (it.result?.exists()!!) {
                    Log.d(ContentValues.TAG, "get user success: ${user.id}")
                } else {
                    subscribeToDB(user, docRef)
                }
            } else {
                Log.d(ContentValues.TAG, "get user failure: ${user.id}")
            }
        }

    }

    override fun subscribeToDB(user: ModelUser, documentReference: DocumentReference) {
        documentReference.set(user).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d(ContentValues.TAG, "User create success: ${user.id}")
            } else {
                Log.d(ContentValues.TAG, "User create failure: ${user.id}")
            }
        }
    }
}
