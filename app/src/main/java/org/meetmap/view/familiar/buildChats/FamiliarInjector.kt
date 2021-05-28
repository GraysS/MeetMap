package org.meetmap.view.familiar.buildChats

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.FirebaseApp
import org.meetmap.data.source.implementations.FirebaseFamiliarRepoImpl
import org.meetmap.data.source.repository.IFamiliarRepository

class FamiliarInjector(application: Application): AndroidViewModel(application) {
    init {
        FirebaseApp.initializeApp(application)
    }

    private fun getFamiliarRepository(): IFamiliarRepository {
        return FirebaseFamiliarRepoImpl()
    }

    fun provideFamiliarViewModelFactory(): FamiliarViewModelFactory =
        FamiliarViewModelFactory(
            getFamiliarRepository()
        )
}