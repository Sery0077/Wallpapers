package sery.vlasenko.wallpapers.ui.base.adapter

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseAdapter :
    ListAdapter<RecyclerItem, BaseAdapter.BaseVH<RecyclerItem>>(BaseDiffCallback) {

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: BaseVH<RecyclerItem>, position: Int) {
        holder.bind(currentList[position])
    }

    override fun submitList(list: List<RecyclerItem?>?) {
        super.submitList(list?.let { ArrayList(it) })
    }

    abstract class BaseVH<T : RecyclerItem>(itemView: ViewBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        abstract fun bind(item: T?)
    }
}

