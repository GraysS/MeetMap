package org.meetmap.view.points.buildPoints

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.FirebaseApp
import org.meetmap.data.source.implementations.FirebasePointRepoImpl
import org.meetmap.data.source.repository.IPointRepository

class PointsInjector(application: Application): AndroidViewModel(application) {
    init {
        FirebaseApp.initializeApp(application)
    }

    private fun getPointRepository(): IPointRepository {
        return FirebasePointRepoImpl()
    }

    fun providePointsViewModelFactory(): PointsViewModelFactory =
        PointsViewModelFactory(
            getPointRepository()
        )
}