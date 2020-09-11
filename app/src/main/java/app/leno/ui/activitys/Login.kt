package app.leno.ui.activitys

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import app.leno.R
import app.leno.ui.bases.BaseAuth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch

class Login : BaseAuth(), View.OnClickListener {

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppThemeActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

    }

    override fun onClick(item: View?) {
        when (item?.id) {
            R.id.loginButton -> launch { loginUser() }
            R.id.googleButton -> launch { googleLogin() }
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