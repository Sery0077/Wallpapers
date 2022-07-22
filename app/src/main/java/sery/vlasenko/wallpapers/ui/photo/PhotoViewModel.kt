package sery.vlasenko.wallpapers.ui.photo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import sery.vlasenko.wallpapers.model.pojo.Photo
import sery.vlasenko.wallpapers.model.repository.photos.PhotoRepository
import sery.vlasenko.wallpapers.ui.base.BaseViewModel

class PhotoViewModel @AssistedInject constructor(
    @Assisted("photoId") private val photoId: String,
    private val repository: PhotoRepository
) : BaseViewModel() {

    init {
        getPhoto()
    }

    private val _state: MutableLiveData<PhotoState> =
        MutableLiveData(PhotoState.DataLoading)
    val state: LiveData<PhotoState> = _state

    private fun getPhoto() {
        repository.getPhotoInfo(photoId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    onSuccess(it)
                },
                onError = {
                    onError(it.message)
                }
            )
    }

    private fun onSuccess(photo: Photo) {
        _state.postValue(PhotoState.DataLoaded(photo))
    }

    private fun onError(error: String?) {
        _state.postValue(PhotoState.DataLoadError(error))
    }

    fun onErrorClick() {
        getPhoto()
    }

    class PhotoViewModelFactory @AssistedInject constructor(
        @Assisted("photoId") private val photoId: String,
        private val assistedFactory: PhotoViewModelAssistedFactory,
    ) : ViewModelProvider.AndroidViewModelFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            return assistedFactory.create(photoId) as T
        }

        @AssistedFactory
        interface PhotoViewModelAssistedFactory {
            fun create(@Assisted("photoId") photoId: String): PhotoViewModel
        }
    }
}