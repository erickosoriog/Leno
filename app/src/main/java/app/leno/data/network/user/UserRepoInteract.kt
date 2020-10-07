package app.leno.data.network.user

import app.leno.data.model.ModelUser
import com.google.firebase.firestore.DocumentReference

interface UserRepoInteract {

    fun createUserDB(username: String, email: String)
    fun subscribeToDB(user: ModelUser, documentReference: DocumentReference)
}