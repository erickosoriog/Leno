package app.leno.startApp

import android.content.ContentValues
import android.content.Intent
import android.util.Log
import app.leno.base.BaseActivity
import app.leno.ui.MainActivity
import app.leno.ui.Welcome
import com.google.firebase.auth.FirebaseAuth

class StateCurrentUser : BaseActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onStart() {
        super.onStart()
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        if (user != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            Log.d(ContentValues.TAG, "Current User logged: ${user.uid}")
            finish()

        } else {

            val intent = Intent(this, Welcome::class.java)
            startActivity(intent)
            Log.d(ContentValues.TAG, "Current User not logged: ${user?.uid}")
            finish()

        }
    }
}
