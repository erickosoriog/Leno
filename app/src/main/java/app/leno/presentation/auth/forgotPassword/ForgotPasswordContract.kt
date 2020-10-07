package app.leno.presentation.auth.forgotPassword

interface ForgotPasswordContract {

    interface ForgotView {
        fun showError(errorMsg: String?)
        fun navigateToLogin()
        fun forgotPassword()
    }

    interface Presenter {
        fun attachView(view: ForgotView)
        fun onDetachView()
        fun onDetachJob()
        fun isViewAttach(): Boolean
        fun sendPasswordResetEmail(email: String)

    }
}