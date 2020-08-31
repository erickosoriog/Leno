package app.leno.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import app.leno.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import kotlinx.android.synthetic.main.bottomsheet_fab.view.*
import java.text.DateFormat
import java.util.*
import kotlin.collections.HashMap

private lateinit var navView: ChipNavigationBar
private lateinit var appBarConfiguration: AppBarConfiguration
private lateinit var inflater: LayoutInflater
private lateinit var layout: View
private lateinit var auth: FirebaseAuth
private lateinit var db: FirebaseFirestore
private var firebaseUserID: String = ""

class MainActivity : AppCompatActivity(), ChipNavigationBar.OnItemSelectedListener {

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        updateUI()

        navView = findViewById(R.id.nav_view)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            // on click add Notes o floating button
            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.bottomsheet_fab, null)
            dialog.setContentView(view)
            dialog.show()

            view.note.setOnClickListener {
                startActivity(Intent(this, NoteLayout::class.java))
                finish()
            }

            view.folder.setOnClickListener {
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
                dialog.dismiss()

            }

        }


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.Home, R.id.Favorite, R.id.calendar, R.id.Profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setOnItemSelectedListener(this)

    }

    override fun onStart() {
        super.onStart()
        val navController = findNavController(R.id.nav_host_fragment)

        if (navController.graph.startDestination == navController.currentDestination?.id!!) {
            navView.setItemSelected(id = R.id.home, dispatchAction = true)
        }
    }

    private fun updateUI() {

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        if (user != null) {

            Toast.makeText(this, "Welcome ${user.email}", Toast.LENGTH_SHORT).show()

        } else {

            startActivity(Intent(this, Welcome::class.java))
            finish()

        }
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

        val folder: EditText = layout.findViewById(R.id.folder_title)

        val title: String = folder.text.toString()
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

        hideKeyboard()
        Toast.makeText(this, "Folder add success", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.toolbar_navigation_menu, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onItemSelected(id: Int) {

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val fab: FloatingActionButton = findViewById(R.id.fab)

        when (id) {

            R.id.home -> {
                navController.navigate(R.id.Home)
                supportActionBar?.show()
                fab.show()
            }

            R.id.favorite -> {
                navController.navigate(R.id.Favorite)
                supportActionBar?.show()
                fab.show()
            }

            R.id.calendar -> {
                navController.navigate(R.id.Calendar)
                supportActionBar?.hide()
                fab.hide()
            }

            R.id.profile -> {
                navController.navigate(R.id.Profile)
                supportActionBar?.hide()
                fab.hide()
            }
        }
    }

    override fun onBackPressed() {

        val navController = findNavController(R.id.nav_host_fragment)

        if (navController.graph.startDestination == navController.currentDestination?.id!!) {
            finish()
        } else if (navController.popBackStack()) {
            navController.navigate(R.id.Home)
            navView.setItemSelected(id = R.id.home, dispatchAction = true)
        }
    }

}

