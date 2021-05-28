package org.meetmap.view.settings.buildSettings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.FirebaseApp
import org.meetmap.data.source.implementations.FirebaseFamiliarRepoImpl
import org.meetmap.data.source.implementations.FirebaseUserRepoImpl
import org.meetmap.data.source.repository.IFamiliarRepository
import org.meetmap.data.source.repository.IUserRepository

class SettingsInjector(application: Application): AndroidViewModel(application) {
    init {
        FirebaseApp.initializeApp(application)
    }

    private fun getFamiliarRepository(): IFamiliarRepository {
        return FirebaseFamiliarRepoImpl()
    }

    private fun getUserRepository(): IUserRepository {
        return FirebaseUserRepoImpl()
    }

    fun provideSettingsViewModelFactory(): SettingsViewModelFactory =
        SettingsViewModelFactory(
            getFamiliarRepository(),
            getUserRepository()
        )
}