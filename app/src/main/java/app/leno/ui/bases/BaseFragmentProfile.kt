package app.leno.ui.bases

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import app.leno.R
import app.leno.ui.activity.Welcome
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_profile.*

abstract class BaseFragmentProfile : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            activity?.window?.statusBarColor =
                resources.getColor(R.color.colorPrimary, activity?.theme)

            activity?.window?.decorView?.systemUiVisibility =
                0 or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }

        return inflater.inflate(getLayout(), container, false)
    }

    abstract fun getLayout(): Int

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