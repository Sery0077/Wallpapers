package sery.vlasenko.wallpapers.ui.photos

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import sery.vlasenko.wallpapers.App
import sery.vlasenko.wallpapers.R
import sery.vlasenko.wallpapers.databinding.FragmentPhotosBinding
import sery.vlasenko.wallpapers.ui.base.BaseBindingFragment
import sery.vlasenko.wallpapers.ui.photos.adapter.PhotosAdapter
import sery.vlasenko.wallpapers.utils.SnackBarHelper
import javax.inject.Inject

@AndroidEntryPoint
class PhotosFragment :
    BaseBindingFragment<FragmentPhotosBinding, PhotosViewModel>(FragmentPhotosBinding::inflate),
    PhotosAdapter.ClickListener {


    private val adapter = PhotosAdapter(this)
    private val topicId: String by lazy {
        arguments?.getString(App.applicationContext().getString(R.string.topic_id_key)) ?: ""
    }

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
                adapter.submitList(state.data)
                binding.progressBar.visibility = View.GONE
                binding.rvPhotos.visibility = View.VISIBLE
            }
            is PhotosState.DataLoadError -> {
                SnackBarHelper.errorSnackBar(binding.root) {
                    model.onErrorClick()
                }
            }
            PhotosState.DataLoading -> {

            }
        }
    }

    override fun onItemClick(pos: Int) {
        model.onItemClick(pos)
    }
}