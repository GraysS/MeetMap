package org.meetmap.view.familiar

sealed class FamiliarEvent {
    object OnStart : FamiliarEvent()
    data class OnFamiliarItemClick(val position: Int) : FamiliarEvent()
}