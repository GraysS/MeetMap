package org.meetmap.view.message.buildMessages

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.FirebaseApp
import org.meetmap.data.source.implementations.FirebaseMessageRepoImpl
import org.meetmap.data.source.repository.IMessageRepository

class MessagesInjector(application: Application): AndroidViewModel(application) {
    init {
        FirebaseApp.initializeApp(application)
    }

    private fun getMessageRepository(): IMessageRepository {
        return FirebaseMessageRepoImpl()
    }

    fun provideMessagesViewModelFactory(): MessagesViewModelFactory =
        MessagesViewModelFactory(
            getMessageRepository()
        )
}