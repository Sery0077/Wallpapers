package sery.vlasenko.wallpapers.model.pojo

import sery.vlasenko.wallpapers.ui.base.adapter.RecyclerItem

data class Topic(
    override val id: String,
    val title: String,
    val preview_photos: List<Photo>
) : RecyclerItem