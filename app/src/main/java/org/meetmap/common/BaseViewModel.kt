package org.meetmap.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel<T>(private val uiContext: CoroutineContext) : ViewModel(), CoroutineScope {
    abstract fun handleEvent(event: T)

    //cancellation
    private var jobTracker: Job = Job()

    private val startAnimation = MutableLiveData<Boolean>()
    val stAnimation: LiveData<Boolean> get() = startAnimation
    protected val endAnimation = MutableLiveData<Boolean>()
    val edAnimation: LiveData<Boolean> get() = endAnimation
    private val errorText = MutableLiveData<String>()
    val errorTx: LiveData<String> get() = errorText
    private val successText = MutableLiveData<Int>()
    val successTx: LiveData<Int> get() = successText

    protected fun showLoadingState() {
        startAnimation.value = true
    }

    protected fun showSuccessState(message: Int) {
        endAnimation.value = false
        successText.value = message
    }

    protected fun showErrorState(message: String) {
        endAnimation.value = false
        errorText.value = message
    }

    override val coroutineContext: CoroutineContext
        get() = uiContext + jobTracker

}