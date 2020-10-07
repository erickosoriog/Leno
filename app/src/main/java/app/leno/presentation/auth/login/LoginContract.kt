package app.leno.presentation.auth.login

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

interface LoginContract {

    interface LoginView {
        fun showError(errorMsg: String?)
        fun showProgressBar()
        fun hideProgressBar()
        fun navigationToMain()
        fun navigationToSignUp()
        fun navigationToForgot()
        fun signInWithEmailAndPassword()
        fun signInWithGoogle()
        fun configureGoogleSignIn()
        fun firebaseAuthWithGoogle(account: GoogleSignInAccount)
    }

    interface Presenter {
        fun attachView(view: LoginView)
        fun onDetachView()
        fun onDetachJob()
        fun isViewAttach(): Boolean
        fun checkFields(email: String, password: String): Boolean
        fun signInWithEmailAndPassword(email: String, password: String)
        fun signInWithGoogle(account: GoogleSignInAccount)

    }
}