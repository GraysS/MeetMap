package org.meetmap.view.familiar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import org.meetmap.common.BaseViewModel
import org.meetmap.data.model.domain.Familiar
import org.meetmap.common.Result
import org.meetmap.data.source.repository.IFamiliarRepository
import kotlin.coroutines.CoroutineContext

class FamiliarViewModel(
    private val familiarRepo: IFamiliarRepository,
    uiContext: CoroutineContext
) : BaseViewModel<FamiliarEvent>(uiContext) {

    private val familiarListState = MutableLiveData<List<Familiar>?>()
    val familiarList: LiveData<List<Familiar>?> get() = familiarListState

    private val familiarState = MutableLiveData<Familiar>()
    val familiar: LiveData<Familiar> get() = familiarState

    override fun handleEvent(event: FamiliarEvent) {
        when(event) {
            is FamiliarEvent.OnStart -> getFamiliars()
            is FamiliarEvent.OnFamiliarItemClick -> goMessage(event.position)
        }
    }

    private fun goMessage(position: Int) {
        familiarState.value = familiarList.value!![position]
    }

    private fun getFamiliars() = launch {
        when(val familiarsResult = familiarRepo.getFamiliars()) {
            is Result.Value -> familiarListState.value = familiarsResult.value
            is Result.Error -> showErrorState(familiarsResult.error.message ?: "")
        }
    }
}