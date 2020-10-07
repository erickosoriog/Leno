package app.leno.presentation.welcome

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

interface WelcomeContract {

    interface WelcomeView {
        fun showError(errorMsg: String?)
        fun navigateToSignIn()
        fun navigateToSignUp()
        fun navigateToMain()
        fun signInWithGoogle()
        fun configureGoogleSignIn()
        fun firebaseAuthWithGoogle(account: GoogleSignInAccount)
    }

    interface Presenter {
        fun attachView(view: WelcomeView)
        fun onDetachView()
        fun onDetachJob()
        fun isViewAttach(): Boolean
        fun signInWithGoogle(account: GoogleSignInAccount)
    }
}