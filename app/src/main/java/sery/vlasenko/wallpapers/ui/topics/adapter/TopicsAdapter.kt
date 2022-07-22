package sery.vlasenko.wallpapers.ui.topics.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_topic.view.*
import sery.vlasenko.wallpapers.R
import sery.vlasenko.wallpapers.databinding.ItemLoadingBinding
import sery.vlasenko.wallpapers.databinding.ItemTopicBinding
import sery.vlasenko.wallpapers.model.pojo.Topic
import sery.vlasenko.wallpapers.ui.base.adapter.BaseAdapter
import sery.vlasenko.wallpapers.ui.base.adapter.RecyclerItem

class TopicsAdapter(private val clickListener: ClickListener) : BaseAdapter() {

    companion object {
        const val ITEM = 0
        const val LOADING = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseVH {
        return when (viewType) {
            ITEM -> {
                val binding =
                    ItemTopicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                TopicVH(binding)
            }
            LOADING -> {
                val binding =
                    ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LoadingVH(binding)
            }
            else -> throw IllegalStateException("Unknown item type: $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (currentList[position] == null) LOADING else ITEM
    }

    inner class TopicVH(itemView: ItemTopicBinding) : BaseVH(itemView) {
        override fun bind(item: RecyclerItem?) {
            (item as Topic)
            with(itemView) {
                topic_name.text = item.title

                Glide.with(itemView.context)
                    .load(item.preview_photos[0].urls.thumb)
                    .placeholder(R.drawable.landscape_placeholder)
                    .centerCrop()
                    .into(topic_preview_photo)

                setOnClickListener {
                    clickListener.onItemClick(adapterPosition)
                }
            }
        }
    }

    inner class LoadingVH(itemView: ItemLoadingBinding) : BaseVH(itemView) {
        override fun bind(item: RecyclerItem?) {}
    }

    interface ClickListener {
        fun onItemClick(pos: Int)
    }
}