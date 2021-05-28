package org.meetmap.view.login.buildLogic

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.FirebaseApp
import org.meetmap.data.source.implementations.FirebaseUserRepoImpl
import org.meetmap.data.source.repository.IUserRepository

class LoginInjector(application: Application): AndroidViewModel(application) {
    init {
        FirebaseApp.initializeApp(application)
    }

    private fun getUserRepository(): IUserRepository {
        return FirebaseUserRepoImpl()
    }

    fun provideUserViewModelFactory(): UserViewModelFactory =
        UserViewModelFactory(
            getUserRepository()
        )
}