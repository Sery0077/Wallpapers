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

    fun navigateToPhotosFragment(topicId: String) {
        val args = Bundle()
        args.putString(App.applicationContext().getString(R.string.topic_id_key), topicId)
        navController?.navigate(R.id.action_topicsFragment_to_photosFragment, args)
    }
}