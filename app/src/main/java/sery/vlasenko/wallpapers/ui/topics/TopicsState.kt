package sery.vlasenko.wallpapers.ui.topics

sealed class TopicsState {
    object DataLoading : TopicsState()
    object DataLoaded : TopicsState()
    class DataLoadError(val message: String?) : TopicsState()
}