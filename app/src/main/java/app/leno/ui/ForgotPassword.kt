package app.leno.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import app.leno.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot__password.*

private lateinit var auth: FirebaseAuth
private lateinit var resetpassword: Button

class ForgotPassword : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppThemeActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot__password)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        auth = FirebaseAuth.getInstance()

        supportActionBar?.title = "Forgot Password?"
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)

        resetpassword = findViewById(R.id.reset_password)
        resetpassword.setOnClickListener {
            resetPassword()

        }
    }

    private fun resetPassword() {

        val email: String = email_reset_password.editText?.text.toString()

        if (email.isEmpty()) {

            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show()

        } else {


            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Email send", Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    } else {
                        Toast.makeText(this, task.exception!!.message, Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, Login::class.java))
        finish()
    }
}