package app.leno.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.leno.R
import app.leno.model.ModelData
import kotlinx.android.synthetic.main.items_view_text.view.*

class MainAdapter : ListAdapter<ModelData, RecyclerView.ViewHolder>(IDiffUtilCallBack()) {

    companion object {
        private const val VIEW_TYPE_NOTE: Int = 0
        private const val VIEW_TYPE_FOLDER: Int = 1
    }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(user: ModelData) {
            itemView.title_items.text = user.title
            itemView.date_items.text = user.date
        }
    }

    inner class FolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(user: ModelData) {
            itemView.title_items.text = user.title
            itemView.date_items.text = user.date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {

            VIEW_TYPE_NOTE -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.note_view, parent, false)
                NoteViewHolder(view)
            }

            VIEW_TYPE_FOLDER -> {
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
        when (getItem(position).type) {

            0L -> return VIEW_TYPE_NOTE
            1L -> return VIEW_TYPE_FOLDER
        }
        return 0
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

}