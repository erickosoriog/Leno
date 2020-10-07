package app.leno.presentation.main

import app.leno.domain.main.MainInteract
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainPresenter(private var mainInteract: MainInteract) : MainContract.Presenter,
    CoroutineScope {

    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    var view: MainContract.MainView? = null

    override fun attachView(view: MainContract.MainView) {
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

    override fun createFolder(title: String) {
        launch {
            try {
                mainInteract.createFolderInDB(title)
                if (isViewAttach()) {
                    view?.hideKeyboard()
                    view?.showToast("Folder add success")
                }
            } catch (e: FirebaseExceptionsMain) {
                if (isViewAttach()) {
                    view?.hideKeyboard()
                    view?.showToast(e.message)
                }

            }
        }
    }
}