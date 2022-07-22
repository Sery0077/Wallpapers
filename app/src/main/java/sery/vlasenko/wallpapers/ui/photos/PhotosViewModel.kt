package sery.vlasenko.wallpapers.ui.photos

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
import sery.vlasenko.wallpapers.App
import sery.vlasenko.wallpapers.R
import sery.vlasenko.wallpapers.model.pojo.Photo
import sery.vlasenko.wallpapers.model.repository.photos.PhotoRepository
import sery.vlasenko.wallpapers.ui.base.BaseViewModel
import sery.vlasenko.wallpapers.utils.Router

class PhotosViewModel @AssistedInject constructor(
    @Assisted("topicId") private val topicId: String,
    private val repository: PhotoRepository
) :
    BaseViewModel() {

    private val _state: MutableLiveData<PhotosState> =
        MutableLiveData(PhotosState.DataLoading)
    val state: LiveData<PhotosState> = _state

    private val data: ArrayList<Photo?> = arrayListOf(null)

    private var hasNextPage = true
    private var maxPage = 1

    init {
        getPhotos()
    }

    private fun getPhotos() {
        if (hasNextPage) {
            disposable.add(repository.getTopicsPhotos(topicId, maxPage)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .map { response ->
                    if (response.isSuccessful) {
                        val pagesHeaders = response.headers()
                            .get(App.applicationContext().getString(R.string.header_link_label))
                            ?: ""
                        val headerPageRegex = Regex(
                            App.applicationContext().getString(R.string.header_page_regex)
                        )
                        val nextPage =
                            headerPageRegex.find(pagesHeaders)?.value?.filter { it.isDigit() }
                                ?.toIntOrNull()

                        processNextPage(nextPage)
                    }
                    response
                }
                .subscribeBy(
                    onSuccess = { response ->
                        if (response.isSuccessful) {
                            response.body()?.let {
                                onSuccess(it)
                            }
                        } else {
                            onError(response.errorBody().toString())
                        }
                    }
                )
            )
        }
    }

    private fun onError(error: String?) {
        _state.postValue(PhotosState.DataLoadError(error))
    }

    private fun onSuccess(photos: List<Photo>) {
        data.apply {
            if (data.size > 0) removeLast()
            addAll(photos)
            add(null)
        }

        _state.postValue(PhotosState.DataLoaded(data))
    }

    private fun processNextPage(nextPage: Int?) {
        if (nextPage == null) {
            hasNextPage = false
            data.removeLast()
        } else {
            maxPage++
        }
    }

    fun onErrorClick() {
        getPhotos()
    }

    fun onEndScroll() {
        getPhotos()
    }

    fun onItemClick(pos: Int) {
        Router.navigateToPhotoFragment(data[pos]!!.id)
    }

    class PhotosViewModelFactory @AssistedInject constructor(
        @Assisted("topicId") private val topicId: String,
        private val assistedFactory: PhotosViewModelAssistedFactory,
    ) : ViewModelProvider.AndroidViewModelFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            return assistedFactory.create(topicId) as T
        }

        @AssistedFactory
        interface PhotosViewModelAssistedFactory {
            fun create(@Assisted("topicId") topicId: String): PhotosViewModel
        }
    }
}