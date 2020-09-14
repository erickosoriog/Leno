package app.leno.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import app.leno.R
import app.leno.databinding.ActivityMainBinding
import app.leno.ui.bases.BaseActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import kotlinx.android.synthetic.main.bottomsheet_fab.view.*


class MainActivity : BaseActivity(), ChipNavigationBar.OnItemSelectedListener {

    private lateinit var navView: ChipNavigationBar
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var fab: FloatingActionButton

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController
        navView = binding.navView

        fab = binding.appBarMain.contentMain.fab
        fab.setOnClickListener {
            // on click add DataRepo o floating button

            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.bottomsheet_fab, null)
            dialog.setContentView(view)
            dialog.show()

            view.note.setOnClickListener {
                val intent = Intent(this, NoteLayout::class.java)
                startActivity(intent)
                finish()
            }

            view.folder.setOnClickListener {

                folderViewDB()
                dialog.dismiss()

            }
        }

        val toolbar: Toolbar = binding.appBarMain.toolbar
        setSupportActionBar(toolbar)

        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.Home, R.id.Favorite, R.id.calendar, R.id.Profile)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setOnItemSelectedListener(this)

        // finish onCreate
    }

    override fun onStart() {
        super.onStart()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        val navController = findNavController(R.id.nav_host_fragment)
        if (navController.graph.startDestination == navController.currentDestination?.id!!) {
            navView.setItemSelected(id = R.id.home)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onItemSelected(id: Int) {
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
                fab.visibility = View.GONE
            }

            R.id.profile -> {
                navController.navigate(R.id.Profile)
                supportActionBar?.hide()
                fab.visibility = View.GONE
            }
        }
    }

    override fun onBackPressed() {

        val navController = findNavController(R.id.nav_host_fragment)

        if (navController.graph.startDestination == navController.currentDestination?.id!!) {
            finish()
        } else if (navController.popBackStack()) {
            navController.navigate(R.id.Home)
            navView.setItemSelected(id = R.id.home)
        }
    }

}


