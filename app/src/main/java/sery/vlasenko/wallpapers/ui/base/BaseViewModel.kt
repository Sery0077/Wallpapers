package sery.vlasenko.wallpapers.ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job

abstract class BaseViewModel : ViewModel() {
    protected lateinit var job: Job

    override fun onCleared() {
        job.cancel()
        super.onCleared()
    }

    open fun onPause() {
        job.cancel()
    }
}