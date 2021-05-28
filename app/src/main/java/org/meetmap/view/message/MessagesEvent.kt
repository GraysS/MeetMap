package org.meetmap.view.message

import org.meetmap.data.model.domain.Familiar

sealed class MessagesEvent {
    data class OnStartGetMessage(val familiar: Familiar) : MessagesEvent()
    data class OnClickButtonSendMessage(val familiar: Familiar, val text: String) : MessagesEvent()
}
