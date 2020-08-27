package app.leno.notes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.leno.R
import kotlinx.android.synthetic.main.folder_view.view.*
import kotlinx.android.synthetic.main.note_view.view.*

private const val note_type: Int = 0
private const val folder_type: Int = 1

class MainAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var datalist = mutableListOf<DataNote>()
    fun setListData(data: MutableList<DataNote>) {
        datalist = data
    }

    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(user: DataNote) {
            itemView.title_folder.text = user.title
            itemView.date_folder.text = user.date
        }
    }

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(user: DataNote) {
            itemView.title_note.text = user.title
            itemView.date_note.text = user.date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == note_type) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.note_view, parent, false)
            NoteViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.folder_view, parent, false)
            MainViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return if (datalist.size > 0) {
            datalist.size
        } else {
            0
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (datalist[position].type != 0L) {
            folder_type
        } else {
            note_type
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == 0) {
            (holder as NoteViewHolder).bindView(datalist[position])
        } else {
            (holder as MainViewHolder).bindView(datalist[position])
        }
    }

}