package sery.vlasenko.wallpapers.ui.photos

sealed class PhotosState {
    object DataLoading : PhotosState()
    object DataLoaded : PhotosState()
    class DataLoadError(val message: String?) : PhotosState()
}
