package app.leno.presentation.auth.register

import app.leno.domain.auth.registerinteractor.RegisterInteract
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class RegisterPresenter(private var registerInteract: RegisterInteract) :
    RegisterContract.Presenter, CoroutineScope {

    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    var view: RegisterContract.RegisterView? = null

    override fun attachView(view: RegisterContract.RegisterView) {
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

    override fun checkFields(username: String, email: String, password: String): Boolean {
        return username.isEmpty() && email.isEmpty() && password.isEmpty()
    }

    override fun createUserWithEmailAndPassword(username: String, email: String, password: String) {

        launch {
            view?.showProgressBar()
            try {
                registerInteract.createUserWithEmailAndPassword(username, email, password)
                if (isViewAttach()) {
                    view?.hideProgressBar()
                    view?.navigationToSignIn()
                }
            } catch (e: FirebaseExceptionsRegister) {
                view?.showError(e.message)
                view?.hideProgressBar()
            }
        }
    }
}