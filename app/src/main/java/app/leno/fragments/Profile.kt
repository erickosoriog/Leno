package app.leno.fragments

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import app.leno.R
import app.leno.ui.Welcome
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profile.*


private lateinit var auth: FirebaseAuth
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Profile : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        LogOut.setOnClickListener {
            val alertDialogBuilder =
                activity?.let { it1 ->
                    androidx.appcompat.app.AlertDialog.Builder(
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
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        activity?.window?.statusBarColor = resources.getColor(R.color.colorPrimary, activity?.theme)

        activity?.window?.decorView?.systemUiVisibility =
            0 or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDestroyView() {
        super.onDestroyView()
        activity?.window?.statusBarColor = resources.getColor(R.color.white, activity?.theme)
        activity?.window?.decorView?.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR

    }

}

