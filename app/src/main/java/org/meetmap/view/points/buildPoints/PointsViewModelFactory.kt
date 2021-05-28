package org.meetmap.view.points.buildPoints

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.Dispatchers
import org.meetmap.data.source.repository.IPointRepository
import org.meetmap.view.points.PointsViewModel

class PointsViewModelFactory(private val pointRepo: IPointRepository
): ViewModelProvider.NewInstanceFactory()  {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return PointsViewModel(pointRepo, Dispatchers.Main) as T
    }

}