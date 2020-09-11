package app.leno.ui.activitys

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.util.Linkify
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.leno.R
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_note_layout.*
import kotlinx.android.synthetic.main.app_bar_note.*
import java.text.DateFormat
import java.util.*
import kotlin.collections.HashMap

class NoteLayout : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var firebaseUserID: String = ""
    private lateinit var titleNote: EditText
    private lateinit var textNote: EditText

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
        textNote = findViewById(R.id.InputNote)
        textNote.autoLinkMask = Linkify.ALL
        textNote.movementMethod
        Linkify.addLinks(textNote, Linkify.WEB_URLS)

        textNote.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    Linkify.addLinks(s, Linkify.WEB_URLS)
                }
            }
        })

        notes_back_btn.setOnClickListener {
            addNoteFireStore()
        }
    }

    override fun onStart() {
        super.onStart()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun addNoteFireStore() {

        var title: String = titleNote.text.toString().trim()
        if (title.isEmpty()) {
            title = "Untitled"
        }

        val text: String = textNote.text.toString()
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
        db.collection("UsersNotes").document(firebaseUserID).collection("DataRepo and Folders")
            .add(userHashMap)
            .addOnSuccessListener { documentReference ->

                Log.d(
                    ContentValues.TAG,
                    "DocumentSnapshot written with ID: ${documentReference.id}"
                )
                Toast.makeText(this, "Note add success", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()

            }
            .addOnFailureListener { e ->
                Log.d(ContentValues.TAG, "Error adding document", e)
            }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}