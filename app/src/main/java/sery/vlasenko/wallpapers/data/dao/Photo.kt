package sery.vlasenko.wallpapers.data.dao

data class Photo(
    val id: String,
    val urls: Urls
)

data class Urls(
    val full: String,
    val thumb: String
)