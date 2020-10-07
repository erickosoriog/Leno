package app.leno.presentation.auth.forgotPassword

import app.leno.domain.auth.Forgotpasswordinteractor.ForgotPasswordInteract
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ForgotPasswordPresenter(private val recoverPasswordInteract: ForgotPasswordInteract) :
    ForgotPasswordContract.Presenter, CoroutineScope {

    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    var view: ForgotPasswordContract.ForgotView? = null

    override fun attachView(view: ForgotPasswordContract.ForgotView) {
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

    override fun sendPasswordResetEmail(email: String) {
        launch {
            try {
                recoverPasswordInteract.sendPasswordResetEmail(email)
                if (isViewAttach()) {
                    view?.navigateToLogin()
                }

            } catch (e: FirebaseExceptionsPassword) {

                view?.showError(e.message)
            }

        }

    }
}