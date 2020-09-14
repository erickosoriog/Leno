package app.leno.startApp

import android.content.ContentValues
import android.content.Intent
import android.util.Log
import app.leno.ui.activity.MainActivity
import app.leno.ui.activity.Welcome
import app.leno.ui.bases.BaseActivity
import com.google.firebase.auth.FirebaseAuth

class StateCurrentUser : BaseActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onStart() {
        super.onStart()
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        if (user != null) {

            Log.d(ContentValues.TAG, "Current User logged: ${user.uid}")
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            finish()

        } else {

            Log.d(ContentValues.TAG, "Current User not logged: ${user?.uid}")
            val intent = Intent(this, Welcome::class.java)
            startActivity(intent)
            finish()

        }
    }
}
