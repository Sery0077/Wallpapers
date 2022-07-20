package sery.vlasenko.wallpapers.ui.topics

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import sery.vlasenko.wallpapers.data.repository.categories.TopicsRepository
import javax.inject.Inject

@HiltViewModel
class ViewModelTopics @Inject constructor(private val repository: TopicsRepository) : ViewModel() {

}