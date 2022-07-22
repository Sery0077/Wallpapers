package sery.vlasenko.wallpapers.model.repository

import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import sery.vlasenko.wallpapers.model.pojo.Photo
import sery.vlasenko.wallpapers.model.pojo.Topic

interface UnsplashService {
    @GET("topics")
    fun getTopics(@Query("page") page: Int): Single<Response<List<Topic>>>

    @GET("/topics/{topic_id}/photos")
    fun getTopicsPhotos(
        @Path("topic_id") topicId: String,
        @Query("page") page: Int,
    ): Single<Response<List<Photo>>>

    @GET("/photos/{photo_id}")
    fun getPhotoInfo(@Path("photo_id") photoId: String): Single<Photo>
}