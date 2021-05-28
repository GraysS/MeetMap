package org.meetmap.view.meet.buildMeet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.Dispatchers
import org.meetmap.data.source.repository.IPointsFamiliarRepository
import org.meetmap.view.meet.MeetViewModel

class MeetViewModelFactory (private val pointsFamiliarRepo: IPointsFamiliarRepository
): ViewModelProvider.NewInstanceFactory()  {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MeetViewModel(pointsFamiliarRepo, Dispatchers.Main) as T
    }
}