package sery.vlasenko.wallpapers.data.repository

import io.reactivex.rxjava3.core.Observable
import retrofit2.Response
import retrofit2.http.GET
import sery.vlasenko.wallpapers.data.dao.Topic

interface UnsplashService {
    @GET("topics")
    fun getTopics(): Observable<Response<List<Topic>>>
}