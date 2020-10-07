package app.leno.domain.main

import app.leno.presentation.main.FirebaseExceptionsMain
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import java.text.DateFormat
import java.util.*
import kotlin.collections.HashMap
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class MainInteractImpl : MainInteract {

    override suspend fun createFolderInDB(title: String): Unit =
        suspendCancellableCoroutine { continuation ->
            val calendar: Calendar = Calendar.getInstance()
            val date: String = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
                .format(calendar.time)

            val firebaseUserID: String? = FirebaseAuth.getInstance().currentUser?.uid
            val docRef = FirebaseFirestore.getInstance().collection("Users")
                .document(firebaseUserID!!)
                .collection("DataRepo and Folders")

            val userHashMap = HashMap<String, Any>()
            userHashMap["created"] = Timestamp.now()
            userHashMap["date"] = date
            userHashMap["title"] = title
            userHashMap["type"] = 1

            docRef.add(userHashMap).addOnCompleteListener {
                if (it.isSuccessful) {
                    continuation.resume(Unit)
                } else {
                    continuation.resumeWithException(FirebaseExceptionsMain(it.exception?.message))
                }
            }

        }
}