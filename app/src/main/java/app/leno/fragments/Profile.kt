package app.leno.fragments

import android.app.AlertDialog
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
            val alertDialogBuilder = AlertDialog.Builder(activity)
            alertDialogBuilder.setTitle("Log out")
            alertDialogBuilder.setMessage("Are you sure you want to leave?")

            alertDialogBuilder.setNegativeButton("No") { dialog, _ ->
                dialog.cancel()
            }
            alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
                auth = FirebaseAuth.getInstance()
                auth.signOut()
                startActivity(Intent(activity, Welcome::class.java))
            }
            alertDialogBuilder.show()
            return@setOnClickListener
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        activity?.window?.statusBarColor = resources.getColor(R.color.colorPrimary, activity?.theme)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onDestroyView() {
        super.onDestroyView()
        activity?.window?.statusBarColor = resources.getColor(R.color.white, activity?.theme)

    }

}

