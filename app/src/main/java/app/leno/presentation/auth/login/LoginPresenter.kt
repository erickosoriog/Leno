package app.leno.presentation.auth.login

import app.leno.domain.auth.logininteractor.LoginInteract
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class LoginPresenter(private var loginInInteract: LoginInteract) : LoginContract.Presenter,
    CoroutineScope {

    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    var view: LoginContract.LoginView? = null

    override fun attachView(view: LoginContract.LoginView) {
        this.view = view
    }

    override fun onDetachView() {
        view = null
    }

    override fun onDetachJob() {
        coroutineContext.cancel()
    }

    override fun isViewAttach(): Boolean {
        return view != null
    }

    override fun checkFields(email: String, password: String): Boolean {
        return email.isEmpty() || password.isEmpty()
    }

    override fun signInWithEmailAndPassword(email: String, password: String) {
        launch {
            view?.showProgressBar()

            try {
                loginInInteract.signInWithEmailAndPassword(email, password)
                if (isViewAttach()) {
                    view?.hideProgressBar()
                    view?.navigationToMain()
                }

            } catch (e: FirebaseExceptionsLogin) {
                if (isViewAttach()) {
                    view?.showError(e.message)
                    view?.hideProgressBar()
                }

            }
        }


    }

    override fun signInWithGoogle(account: GoogleSignInAccount) {
        launch {
            try {

                loginInInteract.firebaseAuthWithGoogle(account)
                if (isViewAttach()) {
                    view?.navigationToMain()
                }

            } catch (e: FirebaseExceptionsLogin) {
                if (isViewAttach()) {
                    view?.showError(e.message)
                }
            }
        }
    }
}