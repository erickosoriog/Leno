package app.leno.presentation.auth.register

interface RegisterContract {

    interface RegisterView {
        fun showError(errorMsg: String?)
        fun showProgressBar()
        fun hideProgressBar()
        fun navigationToSignIn()
        fun signUp()
    }

    interface Presenter {
        fun attachView(view: RegisterView)
        fun onDetachView()
        fun onDetachJob()
        fun isViewAttach(): Boolean
        fun checkFields(username: String, email: String, password: String): Boolean
        fun createUserWithEmailAndPassword(username: String, email: String, password: String)

    }
}