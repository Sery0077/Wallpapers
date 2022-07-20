package sery.vlasenko.wallpapers.ui.topics

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import sery.vlasenko.wallpapers.databinding.FragmentTopicsBinding
import sery.vlasenko.wallpapers.ui.base.BaseBindingFragment

@AndroidEntryPoint
class FragmentTopics :
    BaseBindingFragment<FragmentTopicsBinding, ViewModelTopics>(FragmentTopicsBinding::inflate) {

    override val model: ViewModelTopics by viewModels()
}