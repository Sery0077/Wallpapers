package sery.vlasenko.wallpapers.ui.photo

import sery.vlasenko.wallpapers.model.pojo.Photo

sealed class PhotoState {
    object DataLoading : PhotoState()
    class DataLoaded(val data: Photo) : PhotoState()
    class DataLoadError(val message: String?) : PhotoState()
}
