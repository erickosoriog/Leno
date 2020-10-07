package app.leno.domain.note

import app.leno.data.model.ModelData
import com.google.firebase.firestore.DocumentReference

interface NoteInteract {

    fun createNoteInDB(title: String, text: String)
    fun updateNoteInDB(id: String, title: String, text: String)
    fun subscribeToDB(user: ModelData, documentReference: DocumentReference)
}