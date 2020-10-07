package app.leno.presentation.welcome

import app.leno.domain.welcome.WelcomeInteract
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class WelcomePresenter(private val welcomeInteract: WelcomeInteract) : WelcomeContract.Presenter,
    CoroutineScope {

    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    var view: WelcomeContract.WelcomeView? = null

    override fun attachView(view: WelcomeContract.WelcomeView) {
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

    override fun signInWithGoogle(account: GoogleSignInAccount) {
        launch {
            try {

                welcomeInteract.firebaseAuthWithGoogle(account)
                if (isViewAttach()) {
                    view?.navigateToMain()
                }

            } catch (e: FirebaseExceptionsWelcome) {
                if (isViewAttach()) {
                    view?.showError(e.message)
                }
            }
        }
    }
}