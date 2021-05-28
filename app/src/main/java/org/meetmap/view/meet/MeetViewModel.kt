package org.meetmap.view.meet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.launch
import org.meetmap.common.BaseViewModel
import org.meetmap.common.Result
import org.meetmap.data.model.domain.Familiar
import org.meetmap.data.model.domain.PointsFamiliar
import org.meetmap.data.source.repository.IPointsFamiliarRepository
import org.meetmap.util.getCalendarTime
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class MeetViewModel(private val repo: IPointsFamiliarRepository,
                    uiContext: CoroutineContext
) : BaseViewModel<MeetEvent>(uiContext) {

    private val familiarState = MutableLiveData<Familiar?>()

    private val messageCollectionReference = MutableLiveData<CollectionReference?>()
    val msgCollectionReference: LiveData<CollectionReference?> get() = messageCollectionReference

    override fun handleEvent(event: MeetEvent) {
        when(event) {
            is MeetEvent.OnStartGetPointsFamiliar -> getPointsFamiliar(event.familiar)
            is MeetEvent.OnLongClickAddMarker -> addMessage(familiarState.value!!,event.latLng)
        }
    }

    fun pointsFamiliarGoLiveData(value: CollectionReference) : LiveData<List<PointsFamiliar>> {
        return PointsFamiliarLiveData(value,familiarState.value!!)
    }

    private fun getPointsFamiliar(familiar: Familiar) = launch {
        when (val gets = repo.getPoints()) {
            is Result.Value -> {
                familiarState.value = familiar
                messageCollectionReference.value = gets.value
            }
            is Result.Error -> {
                showErrorState(gets.error.message ?: "")
            }
        }
    }

    private fun addMessage(familiar: Familiar, latLng: LatLng) = launch {
        when (val result = repo.updateMessage(newPointsFamiliar(familiar,latLng))) {
            is Result.Value -> {
                Timber.d("Success")
            }
            is Result.Error -> {
                showErrorState(result.error.message ?: "")
            }
        }
    }

    private fun newPointsFamiliar(familiar: Familiar, latLng: LatLng): PointsFamiliar {
        return PointsFamiliar(getCalendarTime(),familiar.emailFamiliar,familiar.emailCreator,latLng.latitude,latLng.longitude)
    }


}