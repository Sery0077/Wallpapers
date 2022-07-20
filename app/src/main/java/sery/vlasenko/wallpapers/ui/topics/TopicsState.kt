package sery.vlasenko.wallpapers.ui.topics

import sery.vlasenko.wallpapers.data.dao.Topic

sealed class TopicsState {
    object DataLoading : TopicsState()
    class DataLoaded(val data: List<Topic?>) : TopicsState()
    class DataLoadError(val message: String?) : TopicsState()
}