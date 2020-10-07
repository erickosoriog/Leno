package app.leno.presentation.auth.login

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import app.leno.R
import app.leno.bases.BaseActivity
import app.leno.databinding.ActivityLoginBinding
import app.leno.domain.auth.logininteractor.LoginInteractImpl
import app.leno.presentation.auth.forgotPassword.ForgotPassword
import app.leno.presentation.auth.register.Register
import app.leno.presentation.main.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class Login : BaseActivity(), LoginContract.LoginView, View.OnClickListener {

    companion object {
        const val RC_SIGN_IN = 17
    }

    private lateinit var binding: ActivityLoginBinding
    private lateinit var presenter: LoginPresenter
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = LoginPresenter(LoginInteractImpl())
        presenter.attachView(this)
        configureGoogleSignIn()
        textRegister()

    }

    override fun getLayout(): View {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun textRegister() {

        val register = binding.registerLogin
        val text = "Don\'t have account?  Sign up"
        val ss = SpannableString(text)
        val colorSpan =
            ForegroundColorSpan(resources.getColor(R.color.colorPrimary, this.theme))
        ss.setSpan(colorSpan, 20, 28, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        register.text = ss

    }

    override fun configureGoogleSignIn() {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        googleSignInClient.signOut()
    }

    override fun onClick(item: View?) {
        when (item?.id) {

            R.id.loginButton -> signInWithEmailAndPassword()
            R.id.googleButton -> signInWithGoogle()
            R.id.registerLogin -> navigationToSignUp()
            R.id.forgot_password -> navigationToForgot()

        }
    }

    override fun showError(errorMsg: String?) {
        toast(this, errorMsg)
    }

    override fun showProgressBar() {
        binding.progressLogin.visibility = View.VISIBLE
        binding.loginButton.visibility = View.GONE
        binding.googleButton.visibility = View.GONE

    }

    override fun hideProgressBar() {
        binding.progressLogin.visibility = View.GONE
        binding.loginButton.visibility = View.VISIBLE
        binding.googleButton.visibility = View.VISIBLE
    }

    override fun navigationToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun navigationToSignUp() {
        val intent = Intent(this, Register::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        finish()
    }

    override fun navigationToForgot() {
        val intent = Intent(this, ForgotPassword::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        finish()
    }

    override fun signInWithEmailAndPassword() {

        val email: String = binding.emailLogin.editText?.text.toString().trim()
        val password: String = binding.passwordLogin.editText?.text.toString().trim()

        if (presenter.checkFields(email, password))
            toast(this, "Please enter email and password")
        else
            presenter.signInWithEmailAndPassword(email, password)
    }

    override fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        presenter.signInWithGoogle(account)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
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

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.onDetachView()
        presenter.onDetachJob()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetachView()
        presenter.onDetachJob()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )
    }
}