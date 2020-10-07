package app.leno.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.leno.data.model.ModelData
import app.leno.databinding.FolderViewBinding
import app.leno.databinding.NoteViewBinding

class MainAdapter(
    private val context: Context,
    private val itemClickListener: OnItemClickListener
) :
    ListAdapter<ModelData, RecyclerView.ViewHolder>(IDiffUtilCallBack()) {

    companion object {
        private const val VIEW_TYPE_NOTE: Int = 0
        private const val VIEW_TYPE_FOLDER: Int = 1

    }

    interface OnItemClickListener {
        fun onClickNote(item: ModelData)
        fun onclickFolder(item: ModelData)
    }

    inner class NoteViewHolder(val binding: NoteViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(user: ModelData) = with(binding) {
            binding.root.setOnClickListener { itemClickListener.onClickNote(getItem(adapterPosition)) }
            include.titleItems.text = user.title
            include.dateItems.text = user.date
        }
    }

    inner class FolderViewHolder(val binding: FolderViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(user: ModelData) = with(binding) {
            binding.root.setOnClickListener {
                itemClickListener.onclickFolder(
                    getItem(adapterPosition)
                )
            }
            include.titleItems.text = user.title
            include.dateItems.text = user.date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {

            VIEW_TYPE_NOTE -> {
                val view = NoteViewBinding.inflate(LayoutInflater.from(context), parent, false)
                NoteViewHolder(view)

            }

            VIEW_TYPE_FOLDER -> {
                val view = FolderViewBinding.inflate(LayoutInflater.from(context), parent, false)
                FolderViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid View type")
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is MainAdapter.NoteViewHolder -> {
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