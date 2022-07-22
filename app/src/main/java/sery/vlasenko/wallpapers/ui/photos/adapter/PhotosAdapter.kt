package sery.vlasenko.wallpapers.ui.photos.adapter

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_photo.view.*
import sery.vlasenko.wallpapers.R
import sery.vlasenko.wallpapers.databinding.ItemLoadingBinding
import sery.vlasenko.wallpapers.databinding.ItemPhotoBinding
import sery.vlasenko.wallpapers.model.pojo.Photo
import sery.vlasenko.wallpapers.ui.base.adapter.BaseAdapter
import sery.vlasenko.wallpapers.ui.base.adapter.RecyclerItem

class PhotosAdapter(private val clickListener: ClickListener) : BaseAdapter() {

    companion object {
        const val ITEM = 0
        const val LOADING = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseVH {
        return when (viewType) {
            ITEM -> {
                val binding =
                    ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                PhotoVH(binding)
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

    inner class PhotoVH(itemView: ItemPhotoBinding) : BaseVH(itemView) {
        override fun bind(item: RecyclerItem?) {
            (item as Photo)

            val circularProgressDrawable = CircularProgressDrawable(itemView.context)

            circularProgressDrawable.apply {
                strokeWidth = 5f
                centerRadius = 30f
                colorFilter =
                    PorterDuffColorFilter(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.teal_200
                        ), PorterDuff.Mode.SRC_IN
                    )
                start()
            }

            Glide.with(itemView.context)
                .load(item.urls.thumb)
                .centerCrop()
                .placeholder(circularProgressDrawable)
                .into(itemView.iv_photo)

            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
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