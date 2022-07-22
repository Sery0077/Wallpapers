package sery.vlasenko.wallpapers.model.repository.photos

import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import sery.vlasenko.wallpapers.model.pojo.Photo
import sery.vlasenko.wallpapers.model.repository.UnsplashService
import javax.inject.Inject

class PhotoRepository @Inject constructor(private val service: UnsplashService) :
    IPhotoRepository {

    override fun getTopicsPhotos(topicId: String, page: Int): Single<Response<List<Photo>>> =
        service.getTopicsPhotos(topicId = topicId, page = page)

    override fun getPhotoInfo(photoId: String): Single<Photo> = service.getPhotoInfo(photoId)
}