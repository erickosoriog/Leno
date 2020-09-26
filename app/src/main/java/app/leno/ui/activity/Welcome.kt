package app.leno.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import app.leno.R
import app.leno.databinding.ActivityWelcomeBinding
import app.leno.ui.bases.BaseAuth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch


class Welcome : BaseAuth(), View.OnClickListener {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val register = binding.registerWelcome
        val text = "Don\'t have account?  Sign up"
        val ss = SpannableString(text)
        val colorSpan =
            ForegroundColorSpan(resources.getColor(R.color.colorPrimary, this.theme))
        ss.setSpan(colorSpan, 20, 28, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        register.text = ss

    }

    override fun onStart() {
        super.onStart()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun onClick(item: View?) {
        when (item?.id) {
            R.id.loginWelcome -> {
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
                overridePendingTransition(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
                )
            }
            R.id.googleWelcome -> launch { googleLogin() }
            R.id.registerWelcome -> {
                val intent = Intent(this, Register::class.java)
                startActivity(intent)
                overridePendingTransition(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
                )
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            if (task.isSuccessful) {

                try {

                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("GoogleLogin", "firebaseAuthWithGoogle:" + account.id)
                    launch {
                        firebaseAuthWithGoogle(account.idToken!!)
                    }

                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("GoogleLogin", "Google sign in failed", e)

                }
            } else {
                Log.w("GoogleLogin", task.exception.toString())
            }
        }
    }


}