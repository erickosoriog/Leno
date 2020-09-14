package app.leno.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.leno.R
import app.leno.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot__password.*

class ForgotPassword : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppThemeActionBar)
        super.onCreate(savedInstanceState)
        val binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()


        binding.resetPassword?.setOnClickListener {
            resetPassword()

        }
    }

    override fun onStart() {
        super.onStart()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
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

        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }
}