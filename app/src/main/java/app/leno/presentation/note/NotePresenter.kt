package app.leno.presentation.note

import android.content.ContentValues
import android.util.Log
import app.leno.domain.note.NoteInteract

class NotePresenter(private val noteInteract: NoteInteract) : NoteContract.Presenter {

    var view: NoteContract.NoteView? = null

    override fun attachView(view: NoteContract.NoteView) {
        this.view = view
    }

    override fun onDetachView() {
        view = null
    }

    override fun isViewAttach(): Boolean {
        return view != null
    }

    override fun createdNoteInDB(title: String, text: String) {

        try {
            noteInteract.createNoteInDB(title, text)
            if (isViewAttach()) {
                view?.navigateToMain()
            }

        } catch (e: Exception) {
            Log.d(ContentValues.TAG, "Error adding document", e.cause)
        }
    }

    override fun updateNoteInDB(id: String, title: String, text: String) {

        try {
            noteInteract.updateNoteInDB(id, title, text)
            if (isViewAttach()) {
                view?.navigateToMain()
            }

        } catch (e: Exception) {

            Log.d(ContentValues.TAG, "Error update document", e.cause)
        }
    }
}