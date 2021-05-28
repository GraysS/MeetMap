package org.meetmap.view.login

sealed class LoginEvent {
    object OnStart : LoginEvent()
    data class OnRegisterButtonClick(val email: String,val password: String) : LoginEvent()
    data class OnAuthButtonClick(val email: String, val password: String) : LoginEvent()
}