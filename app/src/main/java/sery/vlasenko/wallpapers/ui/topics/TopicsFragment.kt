package sery.vlasenko.wallpapers.ui.topics

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import sery.vlasenko.wallpapers.model.pojo.Topic
import sery.vlasenko.wallpapers.databinding.FragmentTopicsBinding
import sery.vlasenko.wallpapers.ui.base.BaseBindingFragment
import sery.vlasenko.wallpapers.ui.topics.adapter.TopicsAdapter
import sery.vlasenko.wallpapers.utils.SnackBarHelper

@AndroidEntryPoint
class TopicsFragment :
    BaseBindingFragment<FragmentTopicsBinding, TopicsViewModel>(FragmentTopicsBinding::inflate),
    TopicsAdapter.ClickListener {

    override val model: TopicsViewModel by viewModels()
    private val adapter = TopicsAdapter(this)

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
                adapter.submitList(state.data)
                binding.progressBar.visibility = View.GONE
                binding.rvTopics.visibility = View.VISIBLE
            }
            is TopicsState.DataLoadError -> {
                SnackBarHelper.errorSnackBar(binding.root) {
                    model.onErrorClick()
                }
            }
            TopicsState.DataLoading -> {

            }
        }
    }

    override fun onItemClick(topic: Topic) {

    }
}