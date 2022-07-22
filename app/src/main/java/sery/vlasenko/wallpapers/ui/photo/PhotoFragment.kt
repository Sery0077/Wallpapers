package sery.vlasenko.wallpapers.ui.photo

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import dagger.hilt.android.AndroidEntryPoint
import sery.vlasenko.wallpapers.App
import sery.vlasenko.wallpapers.R
import sery.vlasenko.wallpapers.databinding.FragmentPhotoBinding
import sery.vlasenko.wallpapers.ui.base.BaseBindingFragment
import sery.vlasenko.wallpapers.utils.SnackBarHelper
import sery.vlasenko.wallpapers.utils.invisible
import sery.vlasenko.wallpapers.utils.visible
import java.io.IOException
import javax.inject.Inject

@AndroidEntryPoint
class PhotoFragment :
    BaseBindingFragment<FragmentPhotoBinding, PhotoViewModel>(FragmentPhotoBinding::inflate) {

    private val photoId: String by lazy {
        arguments?.getString(App.applicationContext().getString(R.string.photo_id_key)) ?: ""
    }

    @Inject
    lateinit var factory: PhotoViewModel.PhotoViewModelFactory.PhotoViewModelAssistedFactory

    override val model: PhotoViewModel by viewModels {
        PhotoViewModel.PhotoViewModelFactory(photoId, factory)
    }


    override fun onStart() {
        super.onStart()

        model.state.observe(viewLifecycleOwner) {
            processState(it)
        }
        setClickers()
    }

    private fun setClickers() {
        binding.butSetAsWallpaper.setOnClickListener {
            model.state.value.let {
                if (it is PhotoState.DataLoaded) {
                    downloadRawPhoto(it.data.urls.raw)
                }
            }
        }
    }

    private fun downloadRawPhoto(photoUrl: String) {
        Glide.with(this)
            .asBitmap()
            .load(photoUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    setWallpaper(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }

    private fun setWallpaper(photo: Bitmap) {
        val wallpaperManager = WallpaperManager.getInstance(requireContext())
        try {
            wallpaperManager.setBitmap(photo)

            Toast.makeText(
                requireContext(),
                requireContext().getString(R.string.success_set_wallpaper),
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: IOException) {
            Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun processState(state: PhotoState) {
        when (state) {
            PhotoState.DataLoading -> {
                with(binding) {
                    progressBar.visible()
                    zivPhoto.invisible()
                    butSetAsWallpaper.invisible()
                }
            }
            is PhotoState.DataLoadError -> {
                SnackBarHelper.errorSnackBar(binding.root) {
                    model.onErrorClick()
                }
            }
            is PhotoState.DataLoaded -> {
                Glide.with(this)
                    .load(state.data.urls.full)
                    .addListener(RequestListener(state.data.urls.full))
                    .into(binding.zivPhoto)
            }
        }
    }

    lateinit var photo: Bitmap

    private fun onLoadPhotoError() {

    }

    private fun onPhotoLoaded() {
        with(binding) {
            progressBar.invisible()
            zivPhoto.visible()
            butSetAsWallpaper.visible()
        }
    }

    inner class RequestListener(private val photoUrl: String) :
        com.bumptech.glide.request.RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            SnackBarHelper.errorSnackBar(binding.root) {
                Glide.with(this@PhotoFragment)
                    .load(photoUrl)
                    .addListener(RequestListener(photoUrl))
                    .into(binding.zivPhoto)
                onLoadPhotoError()
            }
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            onPhotoLoaded()
            return false
        }
    }
}