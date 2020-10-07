package app.leno.presentation.main

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import app.leno.R
import app.leno.bases.BaseActivity
import app.leno.databinding.ActivityFolderBinding
import app.leno.databinding.ActivityMainBinding
import app.leno.databinding.BottomsheetFabBinding
import app.leno.domain.main.MainInteractImpl
import app.leno.presentation.note.Note
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ismaeldivita.chipnavigation.ChipNavigationBar


class MainActivity : BaseActivity(), MainContract.MainView, View.OnClickListener,
    ChipNavigationBar.OnItemSelectedListener {

    private lateinit var navView: ChipNavigationBar
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var presenter: MainPresenter
    private lateinit var fab: FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = MainPresenter(MainInteractImpl())
        presenter.attachView(this)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        navView = binding.navView
        fab = binding.appBarMain.contentMain.fab

        val toolbar: Toolbar = binding.appBarMain.toolbar
        setSupportActionBar(toolbar)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.Home, R.id.Favorite, R.id.calendar, R.id.Profile)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setOnItemSelectedListener(this)

    }

    override fun getLayout(): View {
        binding = ActivityMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        val navController = findNavController(R.id.nav_host_fragment)
        if (navController.graph.startDestination == navController.currentDestination?.id!!) {
            navView.setItemSelected(id = R.id.home)
        }
    }

    override fun onClick(item: View?) {
        when (item?.id) {

            R.id.fab -> showDialog()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.toolbar_navigation_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
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

    override fun showDialog() {

        val dialog = BottomSheetDialog(this)
        val view = BottomsheetFabBinding.inflate(layoutInflater)
        dialog.setContentView(view.root)
        dialog.show()

        view.note.setOnClickListener {
            navigateToNote()
        }

        view.folder.setOnClickListener {
            createFolderView()
            dialog.dismiss()

        }
    }

    override fun showToast(message: String?) {
        toast(this, message)
    }

    override fun navigateToNote() {
        val intent = Intent(this, Note::class.java)
        startActivity(intent)
        finish()
    }

    override fun createFolderView() {

        val alertDialog = AlertDialog.Builder(this)
        val layout = ActivityFolderBinding.inflate(layoutInflater)
        alertDialog.setView(layout.root)
        alertDialog.setTitle("New folder")
        alertDialog.setCancelable(false)

        alertDialog.setPositiveButton("OK") { _: DialogInterface, _: Int ->
            var title = layout.folderTitle.text.toString().trim()
            if (title.isEmpty()) {
                title = "Untitled"
            }
            presenter.createFolder(title)
        }

        alertDialog.setNegativeButton("Cancel") { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.cancel()
            hideKeyboard()
        }

        alertDialog.show()
        showKeyboard()
        layout.folderTitle.requestFocus()

    }

}


