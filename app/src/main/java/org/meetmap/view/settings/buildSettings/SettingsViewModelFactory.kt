package org.meetmap.view.settings.buildSettings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.Dispatchers
import org.meetmap.data.source.repository.IFamiliarRepository
import org.meetmap.data.source.repository.IUserRepository
import org.meetmap.view.settings.SettingsViewModel

class SettingsViewModelFactory(private val familiarRepo: IFamiliarRepository,
                               private val userRepo: IUserRepository
): ViewModelProvider.NewInstanceFactory()  {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return SettingsViewModel(familiarRepo,userRepo, Dispatchers.Main) as T
    }
}