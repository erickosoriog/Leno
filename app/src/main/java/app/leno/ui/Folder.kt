package app.leno.ui

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import app.leno.R
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_folder.*
import java.text.DateFormat
import java.util.*
import kotlin.collections.HashMap

private lateinit var auth: FirebaseAuth
private lateinit var db: FirebaseFirestore
private lateinit var titlefolder: EditText
private var firebaseUserID: String = ""

class Folder : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_folder)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val calendar: Calendar = Calendar.getInstance()
        val datenote: String = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
            .format(calendar.time)

        textView2.text = datenote

        titlefolder = findViewById(R.id.editTextTextPersonName)

        button.setOnClickListener {
            addFolderDB()
        }
    }

    private fun addFolderDB() {

        val title: String = titlefolder.text.toString()
        val calendar: Calendar = Calendar.getInstance()
        val date: String = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
            .format(calendar.time)

        firebaseUserID = auth.currentUser!!.uid
        val userHashMap = HashMap<String, Any>()
        userHashMap["created"] = Timestamp.now()
        userHashMap["date"] = date
        userHashMap["title"] = title
        userHashMap["type"] = 1

        db.collection("UsersNotes").document(firebaseUserID).collection("Notes and Folders")
            .add(userHashMap)
    }
}