package sery.vlasenko.wallpapers.data.dao

import sery.vlasenko.wallpapers.ui.base.adapter.RecyclerItem

data class Topic(
    override val id: String,
    val title: String,
    val preview_photos: List<Photo>
) : RecyclerItem