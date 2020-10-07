package app.leno.presentation.fragments.profile

interface ProfileContract {

    interface ProfileView {

        fun showDialog()
        fun navigateToWelcome()
    }

    interface Presenter {
        fun attachView(view: ProfileView)
        fun onDetachView()
        fun isViewAttach(): Boolean
        fun signOut()
    }
}