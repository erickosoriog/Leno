package app.leno.adapter

import androidx.recyclerview.widget.DiffUtil
import app.leno.model.ModelData

class IDiffUtilCallBack : DiffUtil.ItemCallback<ModelData>() {

    override fun areItemsTheSame(oldItem: ModelData, newItem: ModelData): Boolean {
        return oldItem == newItem

    }

    override fun areContentsTheSame(oldItem: ModelData, newItem: ModelData): Boolean {
        return oldItem.created == newItem.created
    }
}