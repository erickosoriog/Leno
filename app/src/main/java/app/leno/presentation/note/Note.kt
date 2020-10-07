package app.leno.presentation.note

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.util.Linkify
import android.view.View
import android.widget.EditText
import app.leno.R
import app.leno.bases.BaseActivity
import app.leno.data.model.ModelData
import app.leno.databinding.ActivityNoteLayoutBinding
import app.leno.domain.note.NoteInteractImpl
import app.leno.presentation.main.MainActivity
import java.text.DateFormat
import java.util.*

class Note : BaseActivity(), NoteContract.NoteView, View.OnClickListener {

    companion object {
        const val DATA_TEXT = "passed"
    }

    private lateinit var binding: ActivityNoteLayoutBinding
    private lateinit var presenter: NotePresenter
    private lateinit var documentUserID: String
    private lateinit var dataValue: ModelData
    private lateinit var titleNote: EditText
    private lateinit var textNote: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = NotePresenter(NoteInteractImpl())
        presenter.attachView(this)

        titleNote = binding.appBarNote.contentNote.inputNotesTittle
        textNote = binding.appBarNote.contentNote.inputNote


        if (intent.extras != null) {

            dataValue = intent.getParcelableExtra(DATA_TEXT)!!
            binding.created.text = dataValue.date
            titleNote.setText(dataValue.title)
            textNote.setText(dataValue.text)
            documentUserID = dataValue.id.toString()


            binding.appBarNote.notesBackBtn.setOnClickListener {
                updateNoteInDB()
            }

        } else {

            val calendar: Calendar = Calendar.getInstance()
            val datenote: String =
                DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
                    .format(calendar.time)

            binding.created.text = datenote

        }

        textNote.autoLinkMask = Linkify.ALL
        textNote.movementMethod
        Linkify.addLinks(textNote, Linkify.WEB_URLS)

        textNote.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    Linkify.addLinks(s, Linkify.WEB_URLS)
                }
            }
        })

    }

    override fun getLayout(): View {
        binding = ActivityNoteLayoutBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onClick(item: View?) {
        when (item?.id) {
            R.id.notes_back_btn -> createdNoteInDB()
        }
    }

    override fun navigateToMain() {

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun createdNoteInDB() {

        var title: String = titleNote.text.toString()
        if (title.isEmpty()) {
            title = "Untitled"
        }
        val text: String = textNote.text.toString()

        presenter.createdNoteInDB(title, text)
    }

    override fun updateNoteInDB() {

        var title: String = titleNote.text.toString()
        if (title.isEmpty()) {
            title = "Untitled"
        }
        val text: String = textNote.text.toString()

        presenter.updateNoteInDB(documentUserID, title, text)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        startActivity(intent)
        finish()
    }

}