package sery.vlasenko.wallpapers.ui.photos

import sery.vlasenko.wallpapers.model.pojo.Photo

sealed class PhotosState {
    object DataLoading : PhotosState()
    class DataLoaded(val data: List<Photo?>) : PhotosState()
    class DataLoadError(val message: String?) : PhotosState()
}
