package sery.vlasenko.wallpapers.ui.topics

import sery.vlasenko.wallpapers.model.pojo.Topic

data class TopicsItem(
    val nextPage: Int?,
    val topics: List<Topic>
)
