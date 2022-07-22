package sery.vlasenko.wallpapers.ui.photos

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import sery.vlasenko.wallpapers.App
import sery.vlasenko.wallpapers.R
import sery.vlasenko.wallpapers.databinding.FragmentPhotosBinding
import sery.vlasenko.wallpapers.ui.base.BaseBindingFragment
import sery.vlasenko.wallpapers.ui.photos.adapter.PhotosAdapter
import sery.vlasenko.wallpapers.utils.SnackBarHelper
import sery.vlasenko.wallpapers.utils.gone
import sery.vlasenko.wallpapers.utils.visible
import javax.inject.Inject

@AndroidEntryPoint
class PhotosFragment :
    BaseBindingFragment<FragmentPhotosBinding, PhotosViewModel>(FragmentPhotosBinding::inflate),
    PhotosAdapter.ClickListener {


    private val adapter = PhotosAdapter(this)
    private val topicId: String by lazy {
        arguments?.getString(App.applicationContext().getString(R.string.topic_id_key)) ?: ""
    }

    private var errorSnackBar: Snackbar? = null

    @Inject
    lateinit var factory: PhotosViewModel.PhotosViewModelFactory.PhotosViewModelAssistedFactory

    override val model: PhotosViewModel by viewModels {
        PhotosViewModel.PhotosViewModelFactory(topicId, factory)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecycler()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        model.state.observe(viewLifecycleOwner) {
            processState(it)
        }

        model.photos.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun initRecycler() {
        with(binding.rvPhotos) {
            setHasFixedSize(true)
            adapter = this@PhotosFragment.adapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (!recyclerView.canScrollVertically(1)) {
                        model.onEndScroll()
                    }
                }
            })
        }
    }

    private fun processState(state: PhotosState) {
        when (state) {
            is PhotosState.DataLoaded -> {
                errorSnackBar?.dismiss()
                binding.progressBar.visibility = View.GONE
                binding.rvPhotos.visibility = View.VISIBLE
            }
            is PhotosState.DataLoadError -> {
                binding.progressBar.gone()
                errorSnackBar = SnackBarHelper.errorSnackBar(binding.root) {
                    model.onErrorClick()
                }
                errorSnackBar?.show()
            }
            PhotosState.DataLoading -> {
                errorSnackBar?.dismiss()
                binding.progressBar.visible()
                binding.rvPhotos.gone()
            }
        }
    }

    override fun onItemClick(pos: Int) {
        model.onItemClick(pos)
    }
}