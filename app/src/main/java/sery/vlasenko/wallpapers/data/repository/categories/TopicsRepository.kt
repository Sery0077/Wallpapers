package sery.vlasenko.wallpapers.data.repository.categories

import io.reactivex.rxjava3.core.Observable
import retrofit2.Response
import sery.vlasenko.wallpapers.data.dao.Topic
import sery.vlasenko.wallpapers.data.repository.UnsplashService
import javax.inject.Inject

class TopicsRepository @Inject constructor(private val service: UnsplashService) :
    ITopicsRepository {

    override fun getTopics(page: Int?): Observable<Response<List<Topic>>> = service.getTopics()

}