package org.meetmap.view.message.buildMessages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.Dispatchers
import org.meetmap.data.source.repository.IMessageRepository
import org.meetmap.view.message.MessagesViewModel

class MessagesViewModelFactory(private val messageRepo: IMessageRepository
): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MessagesViewModel(messageRepo, Dispatchers.Main) as T
    }
}