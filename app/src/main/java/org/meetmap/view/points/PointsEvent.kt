package org.meetmap.view.points

import com.google.android.gms.maps.model.LatLng

sealed class PointsEvent {
    object OnStart : PointsEvent()
    data class OnLongClickAddMarker(val latLng: LatLng) : PointsEvent()
}
