package sery.vlasenko.wallpapers.model.repository.topics

import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import sery.vlasenko.wallpapers.model.pojo.Topic
import sery.vlasenko.wallpapers.model.repository.UnsplashService
import javax.inject.Inject

class TopicRepository @Inject constructor(private val service: UnsplashService) :
    ITopicRepository {

    override fun getTopics(page: Int): Single<Response<List<Topic>>> = service.getTopics(page)

}