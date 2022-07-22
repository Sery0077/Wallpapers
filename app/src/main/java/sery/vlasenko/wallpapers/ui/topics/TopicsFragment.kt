package sery.vlasenko.wallpapers.ui.topics

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import sery.vlasenko.wallpapers.databinding.FragmentTopicsBinding
import sery.vlasenko.wallpapers.ui.base.BaseBindingFragment
import sery.vlasenko.wallpapers.ui.topics.adapter.TopicsAdapter
import sery.vlasenko.wallpapers.utils.SnackBarHelper
import sery.vlasenko.wallpapers.utils.gone
import sery.vlasenko.wallpapers.utils.visible

@AndroidEntryPoint
class TopicsFragment :
    BaseBindingFragment<FragmentTopicsBinding, TopicsViewModel>(FragmentTopicsBinding::inflate),
    TopicsAdapter.ClickListener {

    override val model: TopicsViewModel by viewModels()
    private val adapter = TopicsAdapter(this)

    private var errorSnackBar: Snackbar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecycler()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        model.state.observe(viewLifecycleOwner) {
            processState(it)
        }

        model.topics.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun initRecycler() {
        with(binding.rvTopics) {
            setHasFixedSize(true)
            adapter = this@TopicsFragment.adapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (!recyclerView.canScrollVertically(1)) {
                        model.onEndScroll()
                    }
                }
            })
        }
    }

    private fun processState(state: TopicsState) {
        when (state) {
            is TopicsState.DataLoaded -> {
                errorSnackBar?.dismiss()
                binding.progressBar.gone()
                binding.rvTopics.visible()
            }
            is TopicsState.DataLoadError -> {
                binding.progressBar.gone()
                errorSnackBar = SnackBarHelper.errorSnackBar(binding.root) {
                    model.onErrorClick()
                }
                errorSnackBar?.show()
            }
            TopicsState.DataLoading -> {
                errorSnackBar?.dismiss()
                binding.progressBar.visible()
                binding.rvTopics.gone()
            }
        }
    }

    override fun onItemClick(pos: Int) {
        model.onTopicClick(pos)
    }
}