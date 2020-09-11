package app.leno.ui.bases

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import app.leno.R
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.text.DateFormat
import java.util.*
import kotlin.collections.HashMap
import kotlin.coroutines.CoroutineContext

abstract class BaseActivity : AppCompatActivity(), CoroutineScope {

    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var inflater: LayoutInflater
    private lateinit var layout: View

    @SuppressLint("InflateParams")
    fun folderViewDB() {

        val alertDialog = AlertDialog.Builder(this)
        inflater = this.layoutInflater
        layout = inflater.inflate(R.layout.activity_folder, null)
        alertDialog.setView(layout)
        alertDialog.setTitle("New folder")
        alertDialog.setCancelable(false)
        val folder: EditText = layout.findViewById(R.id.folder_title)


        alertDialog.setPositiveButton("OK") { _: DialogInterface, _: Int ->
            addFolderDB()

        }

        alertDialog.setNegativeButton("Cancel") { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.cancel()
            hideKeyboard()
        }

        alertDialog.show()
        folder.requestFocus()
        showKeyboard()
    }

    private fun showKeyboard() {

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    private fun hideKeyboard() {

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }

    private fun addFolderDB() {

        val auth = FirebaseAuth.getInstance()

        val folder: EditText = layout.findViewById(R.id.folder_title)

        val title: String = folder.text.toString()
        val calendar: Calendar = Calendar.getInstance()
        val date: String = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
            .format(calendar.time)

        val firebaseUserID = auth.currentUser!!.uid
        val userHashMap = HashMap<String, Any>()
        userHashMap["created"] = Timestamp.now()
        userHashMap["date"] = date
        userHashMap["title"] = title
        userHashMap["type"] = 1

        FirebaseFirestore.getInstance().collection("UsersNotes").document(firebaseUserID)
            .collection("DataRepo and Folders")
            .add(userHashMap)

        Toast.makeText(this, "Folder add success", Toast.LENGTH_SHORT).show()
    }

}