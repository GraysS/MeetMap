package org.meetmap.view.familiar.buildChats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.Dispatchers
import org.meetmap.data.source.repository.IFamiliarRepository
import org.meetmap.view.familiar.FamiliarViewModel

class FamiliarViewModelFactory(private val familiarRepo: IFamiliarRepository
): ViewModelProvider.NewInstanceFactory()  {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return FamiliarViewModel(familiarRepo, Dispatchers.Main) as T
    }
}