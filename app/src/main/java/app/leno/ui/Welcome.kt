package app.leno.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import app.leno.R

private lateinit var login_welcome: Button
private lateinit var register_welcome: Button

class Welcome : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        clickBottoms()


    }

    private fun clickBottoms() {

        login_welcome = findViewById(R.id.login)
        login_welcome.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
            overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        }
        register_welcome = findViewById(R.id.register_login)
        register_welcome.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
            overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        }

    }
}