package app.leno.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import app.leno.R
import app.leno.databinding.ActivityLoginBinding
import app.leno.ui.bases.BaseAuth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch

class Login : BaseAuth(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppThemeActionBar)
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onClick(item: View?) {
        when (item?.id) {
            R.id.loginButton -> launch { loginUser() }
            R.id.googleButton -> launch { googleLogin() }
            R.id.forgot_password -> {
                val intent = Intent(this, ForgotPassword::class.java)
                startActivity(intent)
                finish()
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

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(
            R.anim.slide_in_left,
            R.anim.slide_out_right
        )
    }

}