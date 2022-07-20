package sery.vlasenko.wallpapers.data.repository

import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import sery.vlasenko.wallpapers.data.dao.Topic

interface UnsplashService {
    @GET("topics")
    fun getTopics(@Query("page") page: Int): Single<Response<List<Topic>>>
}