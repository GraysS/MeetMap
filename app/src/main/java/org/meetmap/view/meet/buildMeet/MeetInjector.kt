package org.meetmap.view.meet.buildMeet

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.FirebaseApp
import org.meetmap.data.source.implementations.FirebasePointsFamiliarRepoImpl
import org.meetmap.data.source.repository.IPointsFamiliarRepository

class MeetInjector(application: Application): AndroidViewModel(application) {
    init {
        FirebaseApp.initializeApp(application)
    }

    private fun getPointsFamiliarRepository(): IPointsFamiliarRepository {
        return FirebasePointsFamiliarRepoImpl()
    }

    fun provideMeetViewModelFactory(): MeetViewModelFactory =
        MeetViewModelFactory(getPointsFamiliarRepository())
}