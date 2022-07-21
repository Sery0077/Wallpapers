package sery.vlasenko.wallpapers.model.pojo

data class Photo(
    val id: String,
    val urls: Urls
)

data class Urls(
    val full: String,
    val thumb: String
)