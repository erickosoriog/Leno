package app.leno.presentation.note

interface NoteContract {

    interface NoteView {

        fun navigateToMain()
        fun createdNoteInDB()
        fun updateNoteInDB()
    }

    interface Presenter {
        fun attachView(view: NoteView)
        fun onDetachView()
        fun isViewAttach(): Boolean
        fun createdNoteInDB(title: String, text: String)
        fun updateNoteInDB(id: String, title: String, text: String)
    }
}