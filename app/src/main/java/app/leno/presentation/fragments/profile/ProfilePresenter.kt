package app.leno.presentation.fragments.profile

import com.google.firebase.auth.FirebaseAuth

class ProfilePresenter : ProfileContract.Presenter {

    var view: ProfileContract.ProfileView? = null

    override fun attachView(view: ProfileContract.ProfileView) {
        this.view = view
    }

    override fun onDetachView() {
        view = null
    }

    override fun isViewAttach(): Boolean {
        return view != null
    }

    override fun signOut() {
        FirebaseAuth.getInstance().signOut()
        view?.navigateToWelcome()
    }
}