package org.meetmap.view.meet

import com.google.android.gms.maps.model.LatLng
import org.meetmap.data.model.domain.Familiar

sealed class MeetEvent {
    data class OnStartGetPointsFamiliar(val familiar: Familiar) : MeetEvent()
    data class OnLongClickAddMarker(val latLng: LatLng) : MeetEvent()
}
