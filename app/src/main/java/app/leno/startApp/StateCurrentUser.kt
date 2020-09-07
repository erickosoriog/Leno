package app.leno.startApp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.leno.ui.MainActivity
import app.leno.ui.Welcome
import com.google.firebase.auth.FirebaseAuth

class StateCurrentUser : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        if (user != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()

        } else {
            startActivity(Intent(this, Welcome::class.java))
            finish()
        }
    }
}
