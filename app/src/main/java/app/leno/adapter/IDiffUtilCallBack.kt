package app.leno.adapter

import androidx.recyclerview.widget.DiffUtil
import app.leno.model.DataNote

class IDiffUtilCallBack : DiffUtil.ItemCallback<DataNote>() {

    override fun areItemsTheSame(oldItem: DataNote, newItem: DataNote): Boolean {
        return oldItem.created == newItem.created

    }

    override fun areContentsTheSame(oldItem: DataNote, newItem: DataNote): Boolean {
        return oldItem == newItem
    }
}