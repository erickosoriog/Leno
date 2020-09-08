package app.leno.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.leno.R
import app.leno.model.DataNote
import kotlinx.android.synthetic.main.items_view_text.view.*

class MainAdapter : ListAdapter<DataNote, RecyclerView.ViewHolder>(IDiffUtilCallBack()) {

    companion object {
        private const val NoteType: Int = 0
        private const val FolderType: Int = 1
    }

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(user: DataNote) {
            itemView.title_items.text = user.title
            itemView.date_items.text = user.date
        }
    }

    class FolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(user: DataNote) {
            itemView.title_items.text = user.title
            itemView.date_items.text = user.date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {

            NoteType -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.note_view, parent, false)
                NoteViewHolder(view)
            }

            FolderType -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.folder_view, parent, false)
                FolderViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is NoteViewHolder -> {
                holder.bindView(item)
            }
            is FolderViewHolder -> {
                holder.bindView(item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item.type != 0L) {

            FolderType

        } else {

            NoteType
        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

}