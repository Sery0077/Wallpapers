package sery.vlasenko.wallpapers.data.dao

data class Topic(
    val id: String,
    val title: String,
    val preview_photos: ArrayList<Photo>
)