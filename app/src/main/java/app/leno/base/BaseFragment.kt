package app.leno.base

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import app.leno.R
import app.leno.ui.Welcome
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_profile.*

abstract class BaseFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    fun profile() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            activity?.window?.statusBarColor =
                resources.getColor(R.color.colorPrimary, activity?.theme)

            activity?.window?.decorView?.systemUiVisibility =
                0 or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
    }

    fun baseFragment() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            activity?.window?.statusBarColor = resources.getColor(R.color.white, activity?.theme)
            activity?.window?.decorView?.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
    }

    fun logOut() {

        LogOut.setOnClickListener {
            val alertDialogBuilder =
                activity?.let { it1 ->
                    AlertDialog.Builder(
                        it1,
                        R.style.AlertDialogTheme
                    )
                }
            alertDialogBuilder?.setTitle("Log out")
            alertDialogBuilder?.setMessage("Are you sure you want to leave?")

            alertDialogBuilder?.setNegativeButton("No") { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.cancel()
            }
            alertDialogBuilder?.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
                signOut()
            }

            alertDialogBuilder?.show()
            return@setOnClickListener
        }
    }

    private fun signOut() {
        auth = FirebaseAuth.getInstance()
        auth.signOut()
        startActivity(Intent(activity, Welcome::class.java))
        clearFindViewByIdCache()
    }

}