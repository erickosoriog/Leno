package app.leno.domain.note

import android.content.ContentValues
import android.util.Log
import app.leno.data.model.ModelData
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.text.DateFormat
import java.util.*

class NoteInteractImpl : NoteInteract {

    private var firebaseUserID: String? = null

    override fun createNoteInDB(title: String, text: String) {

        val calendar: Calendar = Calendar.getInstance()
        val date: String = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
            .format(calendar.time)

        firebaseUserID = FirebaseAuth.getInstance().currentUser?.uid
        val docRef = FirebaseFirestore.getInstance().collection("Users")
            .document(firebaseUserID!!)
            .collection("DataRepo and Folders").document()

        val user = ModelData()
        user.id = docRef.id
        user.title = title
        user.text = text
        user.date = date
        user.created = Timestamp.now()
        user.type = 0

        docRef.get().addOnCompleteListener {
            if (it.isSuccessful) {
                if (it.result?.exists()!!) {
                    Log.d(ContentValues.TAG, "get user note success: ${user.id}")
                } else {
                    subscribeToDB(user, docRef)
                }
            } else {
                Log.d(ContentValues.TAG, "get user note failure: ${user.id}")
            }
        }
    }

    override fun subscribeToDB(user: ModelData, documentReference: DocumentReference) {
        documentReference.set(user).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d(ContentValues.TAG, "User note create success: ${user.id}")
            } else {
                Log.d(ContentValues.TAG, "User note create failure: ${user.id}")
            }
        }
    }

    override fun updateNoteInDB(id: String, title: String, text: String) {
        val calendar: Calendar = Calendar.getInstance()
        val date: String = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
            .format(calendar.time)

        firebaseUserID = FirebaseAuth.getInstance().currentUser?.uid
        val docRef = FirebaseFirestore.getInstance().collection("Users")
            .document(firebaseUserID!!)
            .collection("DataRepo and Folders").document(id)

        val userHashMap = HashMap<String, Any>()
        userHashMap["last edited"] = date
        userHashMap["title"] = title
        userHashMap["text"] = text

        docRef.update(userHashMap).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d(ContentValues.TAG, "User note update success: ${docRef.id}")
            } else {
                Log.d(ContentValues.TAG, "User note update success: ${docRef.id}")
            }
        }
    }
}