package sery.vlasenko.wallpapers.model.pojo

import sery.vlasenko.wallpapers.ui.base.adapter.RecyclerItem

data class Photo(
    override val id: String,
    val urls: Urls
) : RecyclerItem

data class Urls(
    val raw: String,
    val full: String,
    val thumb: String
)