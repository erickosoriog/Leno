package app.leno.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import app.leno.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.bottomsheet_fab.view.*

private lateinit var navView: ChipNavigationBar
private lateinit var appBarConfiguration: AppBarConfiguration
private lateinit var auth: FirebaseAuth

class MainActivity : AppCompatActivity(), ChipNavigationBar.OnItemSelectedListener {

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        updateUI()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        navView = findViewById(R.id.nav_view)
        navView.setOnItemSelectedListener(this)

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
                startActivity(Intent(this, Folder::class.java))
                finish()
            }

        }

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

        val navController = findNavController(R.id.nav_host_fragment)
        val fab: FloatingActionButton = findViewById(R.id.fab)

        when (id) {

            R.id.home -> {
                navController.navigate(R.id.Home)
                supportActionBar?.show()
                supportActionBar?.setTitle(R.string.Home)
                searchView.visibility = View.VISIBLE
                fab.show()
            }

            R.id.favorite -> {
                navController.navigate(R.id.Favorite)
                supportActionBar?.show()
                supportActionBar?.setTitle(R.string.Favorite)
                searchView.visibility = View.VISIBLE
                fab.show()
            }

            R.id.calendar -> {
                navController.navigate(R.id.Calendar)
                supportActionBar?.hide()
                supportActionBar?.setTitle(R.string.Search)
                searchView.visibility = View.GONE
                fab.hide()
            }

            R.id.profile -> {
                navController.navigate(R.id.Profile)
                supportActionBar?.hide()
                supportActionBar?.setTitle(R.string.Profile)
                searchView.visibility = View.GONE
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

