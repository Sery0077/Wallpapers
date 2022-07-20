package sery.vlasenko.wallpapers.ui.topics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import sery.vlasenko.wallpapers.App
import sery.vlasenko.wallpapers.R
import sery.vlasenko.wallpapers.data.dao.Topic
import sery.vlasenko.wallpapers.data.repository.categories.TopicsRepository
import sery.vlasenko.wallpapers.ui.ResponseData
import sery.vlasenko.wallpapers.ui.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class ViewModelTopics @Inject constructor(private val repository: TopicsRepository) :
    BaseViewModel() {

    private val _state: MutableLiveData<TopicsState> =
        MutableLiveData(TopicsState.DataLoading)
    val state: LiveData<TopicsState> = _state

    private val data: ArrayList<Topic?> = arrayListOf(null)

    private var hasNextPage = true
    private var maxPage = 1

    init {
        getTopics()
    }

    private fun getTopics() {
        if (hasNextPage) {
            disposable.add(repository.getTopics(maxPage)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .map { response ->
                    if (response.isSuccessful) {
                        val topics = response.body()

                        val pagesHeaders = response.headers()
                            .get(App.applicationContext().getString(R.string.header_link_label))
                            ?: ""
                        val nextPage =
                            Regex(
                                App.applicationContext().getString(R.string.header_page_regex)
                            )
                                .find(pagesHeaders)?.value?.filter { it.isDigit() }?.toIntOrNull()

                        ResponseData(TopicsItem(nextPage, topics!!), null)
                    } else {
                        ResponseData(null, response.message())
                    }
                }
                .subscribeBy(
                    onSuccess = { response ->
                        if (response.data?.topics != null) {
                            onSuccess(response.data)
                        } else {
                            onError(response.error)
                        }
                    }
                )
            )
        }
    }

    private fun onError(error: String?) {
        _state.postValue(TopicsState.DataLoadError(error))
    }

    private fun onSuccess(response: TopicsItem) {
        data.apply {
            removeLast()
            addAll(response.topics)
            add(null)
        }

        _state.postValue(TopicsState.DataLoaded(data))

        processNextPage(response)
    }

    private fun processNextPage(response: TopicsItem) {
        if (response.nextPage == null) {
            hasNextPage = false
            data.removeLast()
        } else {
            maxPage++
        }
    }

    fun onErrorClick() {
        getTopics()
    }

    fun onEndScroll() {
        getTopics()
    }
}