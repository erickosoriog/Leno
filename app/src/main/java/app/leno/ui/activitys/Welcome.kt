package app.leno.ui.activitys

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import app.leno.R


class Welcome : AppCompatActivity() {

    private lateinit var loginWelcome: Button
    private lateinit var registerWelcome: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        clickBottoms()

    }

    override fun onStart() {
        super.onStart()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun clickBottoms() {

        loginWelcome = findViewById(R.id.login)
        loginWelcome.setOnClickListener {

            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        }
        registerWelcome = findViewById(R.id.register_login)
        registerWelcome.setOnClickListener {

            val intent = Intent(this, Register::class.java)
            startActivity(intent)
            overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        }

    }

}