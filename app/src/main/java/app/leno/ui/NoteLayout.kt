package app.leno.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import app.leno.R
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_note_layout.*
import java.text.DateFormat
import java.util.*
import kotlin.collections.HashMap

private lateinit var auth: FirebaseAuth
private lateinit var db: FirebaseFirestore
private var firebaseUserID: String = ""
private lateinit var titleNote: EditText
private lateinit var date: TextView
private lateinit var text: EditText

class NoteLayout : AppCompatActivity() {
    private val e = "Log"

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_layout)


        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val calendar: Calendar = Calendar.getInstance()
        val datenote: String = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
            .format(calendar.time)

        created.text = datenote

        titleNote = findViewById(R.id.InputNotesTittle)
        date = findViewById(R.id.DateTime)
        text = findViewById(R.id.InputNote)

        notes_back_btn.setOnClickListener {
            addNoteDB()
        }
    }

    private fun addNoteDB() {

        val title: String = titleNote.text.toString()
        val text: String = text.text.toString()
        val calendar: Calendar = Calendar.getInstance()
        val date: String = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
            .format(calendar.time)

        val documentHashMap = HashMap<String, Any>()
        documentHashMap["created"] = Timestamp.now()

        firebaseUserID = auth.currentUser!!.uid
        val userHashMap = HashMap<String, Any>()
        userHashMap["created"] = Timestamp.now()
        userHashMap["date"] = date
        userHashMap["title"] = title
        userHashMap["text"] = text
        userHashMap["type"] = 0

        db.collection("UsersNotes").document(firebaseUserID).set(documentHashMap)
        db.collection("UsersNotes").document(firebaseUserID).collection("Notes and Folders")
            .add(userHashMap)
            .addOnSuccessListener { documentReference ->

                Toast.makeText(this, "Note add success", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                Log.d(e, "DocumentSnapshot written with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(this.e, "Error adding document", e)
            }

    }
}