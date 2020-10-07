package app.leno.presentation.main

interface MainContract {

    interface MainView {
        fun showToast(message: String?)
        fun showDialog()
        fun showKeyboard()
        fun hideKeyboard()
        fun navigateToNote()
        fun createFolderView()

    }

    interface Presenter {
        fun attachView(view: MainView)
        fun onDetachView()
        fun onDetachJob()
        fun isViewAttach(): Boolean
        fun createFolder(title: String)
    }
}