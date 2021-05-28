package org.meetmap.view.settings

sealed class SettingsEvent {
    object OnClickButtonLogout : SettingsEvent()
    data class OnClickButtonAddFamiliar(val email: String,val nickname: String) : SettingsEvent()
}
