package org.meetmap.view.settings

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import org.meetmap.common.BaseViewModel
import org.meetmap.common.EMPTY
import org.meetmap.common.FAMILIAR_ADD
import org.meetmap.common.Result
import org.meetmap.data.model.domain.Familiar
import org.meetmap.data.source.repository.IFamiliarRepository
import org.meetmap.data.source.repository.IUserRepository
import org.meetmap.util.getCalendarTime
import kotlin.coroutines.CoroutineContext

class SettingsViewModel(
    private val familiarRepo: IFamiliarRepository,
    private val userRepo: IUserRepository,
    uiContext: CoroutineContext
) : BaseViewModel<SettingsEvent>(uiContext) {
    internal val signOut = MutableLiveData<Boolean>()
    override fun handleEvent(event: SettingsEvent) {
        when(event) {
            is SettingsEvent.OnClickButtonLogout -> logout()
            is SettingsEvent.OnClickButtonAddFamiliar -> saveFamiliar(event.email,event.nickname)
        }
    }
    private fun logout() = launch {
        when(val result = userRepo.signOutCurrentUser()) {
            is Result.Value -> signOut.value = true
            is Result.Error -> showErrorState(result.error.message ?: "")
        }
    }
    private fun saveFamiliar(email: String, nickname: String) = launch {
        if(email.isEmpty() || nickname.isEmpty()) {
            showErrorState(EMPTY)
            return@launch
        }
        when(val result = familiarRepo.updateFamiliar(newFamiliar(email,nickname))) {
            is Result.Value -> showSuccessState(FAMILIAR_ADD)
            is Result.Error -> showErrorState(result.error.message ?: "")
        }
    }
    private fun newFamiliar(email: String, nickname: String): Familiar {
        return Familiar(getCalendarTime(), email,nickname,"")
    }
}