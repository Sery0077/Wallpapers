package sery.vlasenko.wallpapers.data.repository.categories

import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import sery.vlasenko.wallpapers.data.dao.Topic

interface ITopicsRepository {
    fun getTopics(page: Int): Single<Response<List<Topic>>>
}