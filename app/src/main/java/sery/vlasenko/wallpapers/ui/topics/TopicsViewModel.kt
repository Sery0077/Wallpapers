package sery.vlasenko.wallpapers.ui.topics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import sery.vlasenko.wallpapers.App
import sery.vlasenko.wallpapers.R
import sery.vlasenko.wallpapers.model.pojo.Topic
import sery.vlasenko.wallpapers.model.repository.topics.TopicRepository
import sery.vlasenko.wallpapers.ui.base.BaseViewModel
import sery.vlasenko.wallpapers.utils.Router
import javax.inject.Inject

@HiltViewModel
class TopicsViewModel @Inject constructor(private val repository: TopicRepository) :
    BaseViewModel() {

    private val _state: MutableLiveData<TopicsState> =
        MutableLiveData(TopicsState.DataLoading)
    val state: LiveData<TopicsState> = _state

    private val data: ArrayList<Topic?> = arrayListOf(null)

    private val _topics: MutableLiveData<ArrayList<Topic?>> = MutableLiveData()
    val topics: LiveData<ArrayList<Topic?>> = _topics

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
                    },
                    onError = {
                        onError(it.message)
                    }
                )
            )
        }
    }

    private fun onError(error: String?) {
        _state.postValue(TopicsState.DataLoadError(error))
    }

    private fun onSuccess(topics: List<Topic>) {
        data.apply {
            data.removeLast()
            addAll(topics)
            if (hasNextPage) add(null)
        }

        _topics.postValue(data)
        _state.postValue(TopicsState.DataLoaded)
    }

    private fun processNextPage(nextPage: Int?) {
        if (nextPage == null) {
            hasNextPage = false
        } else {
            maxPage++
        }
    }

    fun onTopicClick(pos: Int) {
        data[pos]?.let {
            Router.navigateToPhotosFragment(it.id)
        }
    }

    fun onErrorClick() {
        getTopics()
    }

    fun onEndScroll() {
        getTopics()
    }
}