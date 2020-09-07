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


    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(user: DataNote) {
            itemView.title_items.text = user.title
            itemView.date_items.text = user.date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_view, parent, false)
        return NoteViewHolder(view)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is NoteViewHolder -> {
                holder.bindView(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

}