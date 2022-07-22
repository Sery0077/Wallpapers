package sery.vlasenko.wallpapers.model.repository.photos

import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import sery.vlasenko.wallpapers.model.pojo.Photo

interface IPhotoRepository {
    fun getTopicsPhotos(topicId: String, page: Int): Single<Response<List<Photo>>>
    fun getPhotoInfo(photoId: String): Single<Photo>
}