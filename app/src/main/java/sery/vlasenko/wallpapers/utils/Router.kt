package sery.vlasenko.wallpapers.utils

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.navigation.NavController
import sery.vlasenko.wallpapers.App
import sery.vlasenko.wallpapers.R

object Router {
    @SuppressLint("StaticFieldLeak")
    private var navController: NavController? = null

    fun setNavController(navController: NavController?) {
        this.navController = navController
    }

    fun navigateToPhotosFragment(topicId: String, topicTitle: String) {
        val args = Bundle()
        args.putString(App.applicationContext().getString(R.string.topic_id_key), topicId)
        args.putString(App.applicationContext().getString(R.string.topic_title_key), topicTitle)
        navController?.navigate(R.id.action_topicsFragment_to_photosFragment, args)
    }

    fun navigateToPhotoFragment(photoId: String) {
        val args = Bundle()
        args.putString(App.applicationContext().getString(R.string.photo_id_key), photoId)
        navController?.navigate(R.id.action_photosFragment_to_photoFragment, args)
    }
}