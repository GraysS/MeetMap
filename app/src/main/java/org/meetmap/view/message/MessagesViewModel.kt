package org.meetmap.view.message

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.launch
import org.meetmap.Event
import org.meetmap.EventObserver
import org.meetmap.common.BaseViewModel
import org.meetmap.common.Result
import org.meetmap.data.model.domain.Familiar
import org.meetmap.data.model.domain.Message
import org.meetmap.data.source.repository.IMessageRepository
import org.meetmap.util.getCalendarTime
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class MessagesViewModel(
    private val repo: IMessageRepository,
    uiContext: CoroutineContext
) : BaseViewModel<MessagesEvent>(uiContext) {

    private val familiarState = MutableLiveData<Familiar?>()

    internal val goMaps = MutableLiveData<Event<Boolean>>()

    private val messageCollectionReference = MutableLiveData<CollectionReference?>()
    val msgCollectionReference: LiveData<CollectionReference?> get() = messageCollectionReference

    override fun handleEvent(event: MessagesEvent) {
        when(event) {
            is MessagesEvent.OnStartGetMessage -> getMessage(event.familiar)
            is MessagesEvent.OnClickButtonSendMessage -> addMessage(event.familiar,event.text)
        }
    }

    fun messageGoLiveData(value: CollectionReference) : LiveData<List<Message>> {
        return MessageLiveData(value,familiarState.value!!)
    }

    private fun addMessage(familiar: Familiar, text: String) = launch {

        when (val result = repo.updateMessage(newMessage(familiar,text))) {
            is Result.Value -> {
                Timber.d("Success")
                if(text == "map" || text == "карта") {
                    goMaps.value = Event(true)
                }
            }
            is Result.Error -> {
                showErrorState(result.error.message ?: "")
            }
        }
    }

    private fun newMessage(familiar: Familiar, text: String): Message {
        return Message(getCalendarTime(),familiar.emailFamiliar,familiar.emailCreator,text)
    }

    private fun getMessage(familiar: Familiar) {
        when (val gets = repo.getMessages()) {
            is Result.Value -> {
                familiarState.value = familiar
                messageCollectionReference.value = gets.value
            }
            is Result.Error -> {
                showErrorState(gets.error.message ?: "")
            }
        }
    }

}