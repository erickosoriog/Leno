package app.leno.presentation.welcome

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import app.leno.R
import app.leno.bases.BaseActivity
import app.leno.databinding.ActivityWelcomeBinding
import app.leno.domain.welcome.WelcomeInteractImpl
import app.leno.presentation.auth.login.Login
import app.leno.presentation.auth.register.Register
import app.leno.presentation.main.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth


class Welcome : BaseActivity(), WelcomeContract.WelcomeView, View.OnClickListener {

    companion object {
        const val RC_SIGN_IN = 17
    }

    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var presenter: WelcomePresenter
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = WelcomePresenter(WelcomeInteractImpl())
        presenter.attachView(this)
        configureGoogleSignIn()
        textRegister()

    }

    override fun getLayout(): View {
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            Log.d(ContentValues.TAG, "Current User logged: ${user.uid}")
            navigateToMain()
        } else {
            Log.d(ContentValues.TAG, "Current User not logged: ${user?.uid}")
        }

        overridePendingTransition(
            android.R.anim.fade_in,
            android.R.anim.fade_out
        )
    }

    override fun onClick(item: View?) {
        when (item?.id) {
            R.id.loginWelcome -> navigateToSignIn()
            R.id.googleWelcome -> signInWithGoogle()
            R.id.registerWelcome -> navigateToSignUp()
        }
    }

    private fun textRegister() {

        val register = binding.registerWelcome
        val text = "Don\'t have account?  Sign up"
        val ss = SpannableString(text)
        val colorSpan =
            ForegroundColorSpan(resources.getColor(R.color.colorPrimary, this.theme))
        ss.setSpan(colorSpan, 20, 28, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        register.text = ss
    }

    override fun showError(errorMsg: String?) {
        toast(this, errorMsg)
    }

    override fun navigateToSignIn() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        overridePendingTransition(
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )
        finish()
    }

    override fun navigateToSignUp() {
        val intent = Intent(this, Register::class.java)
        startActivity(intent)
        overridePendingTransition(
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )
        finish()
    }

    override fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun configureGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        googleSignInClient.signOut()
    }

    override fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        presenter.signInWithGoogle(account)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Login.RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            if (task.isSuccessful) {
                try {
                    val account = task.getResult(ApiException::class.java)
                    if (account != null) {
                        Log.d("GoogleLogin", "Google sign in success:" + account.id)
                        firebaseAuthWithGoogle(account)
                    }
                } catch (e: ApiException) {
                    showError(e.message)
                    Log.w("GoogleLogin", "Google sign in failed", e)
                }
            }
        }
    }

}