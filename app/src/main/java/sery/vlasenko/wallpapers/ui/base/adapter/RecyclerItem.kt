package sery.vlasenko.wallpapers.ui.base.adapter

interface RecyclerItem {
    val id: String?
    override fun equals(other: Any?): Boolean
}