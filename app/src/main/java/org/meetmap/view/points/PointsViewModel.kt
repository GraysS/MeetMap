package org.meetmap.view.points

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import org.meetmap.common.BaseViewModel
import org.meetmap.common.MARKER_ADD
import org.meetmap.data.source.repository.IPointRepository
import kotlin.coroutines.CoroutineContext
import org.meetmap.common.Result
import org.meetmap.data.model.domain.Point
import org.meetmap.data.model.domain.User
import org.meetmap.util.getCalendarTime

class PointsViewModel(
    private val repo: IPointRepository,
    uiContext: CoroutineContext
) : BaseViewModel<PointsEvent>(uiContext) {

    private val _listPoints = MutableLiveData<List<Point>?>()
    val listPoints: LiveData<List<Point>?> get() = _listPoints

    private val _point = MutableLiveData<LatLng>()
    val point: LiveData<LatLng> get() = _point

    override fun handleEvent(event: PointsEvent) {
        showLoadingState()
        when(event) {
            is PointsEvent.OnStart -> getPoints()
            is PointsEvent.OnLongClickAddMarker -> addPoint(event.latLng)
        }
    }

    private fun addPoint(latLng: LatLng) = launch {
        when (val result = repo.updatePoint(newPoint(latLng))) {
            is Result.Value ->  {
                _point.value = latLng
                showSuccessState(MARKER_ADD)
            }
            is Result.Error -> showErrorState(result.error.message ?: "")
        }
    }

    private fun getPoints() = launch {
        when (val result = repo.getPoints()) {
            is Result.Value -> { _listPoints.value = result.value }
            is Result.Error -> showErrorState(result.error.message ?: "")
        }
    }

    private fun newPoint(latLng: LatLng): Point {
        return Point(getCalendarTime(),
            User("",""),
            "","",
            latLng.latitude,latLng.longitude)
    }

}