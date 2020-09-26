package app.leno.ui.activity

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.util.Linkify
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.leno.R
import app.leno.data.model.ModelData
import app.leno.databinding.ActivityNoteLayoutBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.DateFormat
import java.util.*
import kotlin.collections.HashMap

class NoteLayout : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val DATA_TEXT = "passed"
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var firebaseUserID: String
    private lateinit var documentUserID: String
    private lateinit var dataValue: ModelData
    private lateinit var titleNote: EditText
    private lateinit var textNote: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNoteLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        firebaseUserID = auth.currentUser?.uid.toString()
        titleNote = binding.appBarNote.contentNote.inputNotesTittle
        textNote = binding.appBarNote.contentNote.inputNote


        if (intent.extras != null) {

            dataValue = intent.getParcelableExtra(DATA_TEXT)!!
            binding.created.text = dataValue.date
            titleNote.setText(dataValue.title)
            textNote.setText(dataValue.text)
            documentUserID = dataValue.id.toString()


            binding.appBarNote.notesBackBtn.setOnClickListener {
                updateData(id = documentUserID)
            }

        } else {

            val calendar: Calendar = Calendar.getInstance()
            val datenote: String =
                DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
                    .format(calendar.time)

            binding.created.text = datenote

        }

        textNote.autoLinkMask = Linkify.ALL
        textNote.movementMethod
        Linkify.addLinks(textNote, Linkify.WEB_URLS)

        textNote.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    Linkify.addLinks(s, Linkify.WEB_URLS)
                }
            }
        })

    }

    override fun onClick(item: View?) {
        when (item?.id) {
            R.id.notes_back_btn -> {
                addNoteFireStore()
            }
        }
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

        val documentID =
            db.collection("UsersNotes").document(firebaseUserID).collection("DataRepo and Folders")
                .document().id


        val userHashMap = HashMap<String, Any>()
        userHashMap["created"] = Timestamp.now()
        userHashMap["date"] = date
        userHashMap["id"] = documentID
        userHashMap["title"] = title
        userHashMap["text"] = text
        userHashMap["type"] = 0

        db.collection("UsersNotes").document(firebaseUserID).set(documentHashMap)
        db.collection("UsersNotes").document(firebaseUserID).collection("DataRepo and Folders")
            .document(documentID)
            .set(userHashMap)
            .addOnSuccessListener { documentReference ->

                Log.d(
                    ContentValues.TAG,
                    "DocumentSnapshot written with ID: $documentReference"
                )
                Toast.makeText(this, "Note add success", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()

            }
            .addOnFailureListener { e ->
                Log.d(ContentValues.TAG, "Error adding document", e)
            }

    }

    private fun updateData(id: String) {

        var title: String = titleNote.text.toString().trim()
        if (title.isEmpty()) {
            title = "Untitled"
        }

        val text: String = textNote.text.toString()
        val calendar: Calendar = Calendar.getInstance()
        val date: String = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
            .format(calendar.time)


        val document = db.collection("UsersNotes").document(firebaseUserID)
            .collection("DataRepo and Folders").document(id)

        val userHashMap = HashMap<String, Any>()
        userHashMap["created"] = Timestamp.now()
        userHashMap["date"] = date
        userHashMap["title"] = title
        userHashMap["text"] = text
        userHashMap["type"] = 0

        document.update(userHashMap).addOnSuccessListener { documentReference ->

            Log.d(ContentValues.TAG, "DocumentSnapshot written with ID: $documentReference")
            startActivity(Intent(this, MainActivity::class.java))
            finish()

        }.addOnFailureListener { e ->
            Log.d(ContentValues.TAG, "Error adding document", e)
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        startActivity(intent)
        finish()
    }

}