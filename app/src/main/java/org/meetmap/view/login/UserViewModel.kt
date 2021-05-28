package org.meetmap.view.login

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import org.meetmap.common.BaseViewModel
import org.meetmap.common.EMPTY
import org.meetmap.data.source.repository.IUserRepository
import kotlin.coroutines.CoroutineContext
import org.meetmap.common.Result
import org.meetmap.data.model.domain.User

class UserViewModel(
    private val repo: IUserRepository,
    uiContext: CoroutineContext
) : BaseViewModel<LoginEvent>(uiContext) {

    private val userState = MutableLiveData<User?>()

    internal val signIn = MutableLiveData<Boolean>()

    override fun handleEvent(event: LoginEvent) {
        showLoadingState()
        when(event) {
            is LoginEvent.OnStart -> getUser()
            is LoginEvent.OnAuthButtonClick -> onAuthButtonClick(event.email,event.password)
            is LoginEvent.OnRegisterButtonClick -> onRegistrationButtonClick(event.email,event.password)
        }
    }

    private fun showSignedOutState() {
        endAnimation.value = false
    }

    private fun showSignedInState() {
        endAnimation.value = false
        signIn.value = true
    }


    private fun getUser() = launch {
        when (val result = repo.getCurrentUser()) {
            is Result.Value -> {
                userState.value = result.value
                if (result.value == null) showSignedOutState()
                else showSignedInState()
            }
            is Result.Error -> showErrorState(result.error.message ?: "")
        }
    }

    private fun onAuthButtonClick(email: String, password: String) = launch {
        if(email.isEmpty() || password.isEmpty()) {
            showErrorState(EMPTY)
            return@launch
        }

        if (userState.value == null)  {
            val result = repo.signInWihEmailAndPassword(email,password)
            if(result is Result.Value) getUser()
            else if(result is Result.Error) showErrorState(result.error.message ?: "")
        }  else {
            signOutUser()
        }
    }

    private fun onRegistrationButtonClick(email: String, password: String) = launch {
        if(email.isEmpty() || password.isEmpty()) {
            showErrorState(EMPTY)
            return@launch
        }

        if(userState.value == null) {
            val result = repo.createUserWithEmailAndPassword(email,password)
            if(result is Result.Value) getUser()
            else if(result is Result.Error) showErrorState(result.error.message ?: "")
        } else {
            signOutUser()
        }
    }

    private fun signOutUser() = launch {
        when (val result = repo.signOutCurrentUser()) {
            is Result.Value -> {
                userState.value = null
                showSignedOutState()
            }
            is Result.Error ->  {
                showErrorState(result.error.message ?: "")
            }
        }
    }

}