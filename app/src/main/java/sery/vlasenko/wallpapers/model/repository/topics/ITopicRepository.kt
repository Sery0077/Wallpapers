package sery.vlasenko.wallpapers.model.repository.topics

import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import sery.vlasenko.wallpapers.model.pojo.Topic

interface ITopicRepository {
    fun getTopics(page: Int): Single<Response<List<Topic>>>
}