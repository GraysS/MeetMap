package org.meetmap.view.login.buildLogic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.Dispatchers
import org.meetmap.data.source.repository.IUserRepository
import org.meetmap.view.login.UserViewModel

class UserViewModelFactory (private val userRepo: IUserRepository
): ViewModelProvider.NewInstanceFactory()  {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return UserViewModel(userRepo, Dispatchers.Main) as T
    }
}