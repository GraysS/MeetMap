package org.meetmap.view.message

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.*
import org.meetmap.common.toMessage
import org.meetmap.data.model.domain.Familiar
import org.meetmap.data.model.domain.Message
import org.meetmap.data.model.network.FirebaseMessage

class MessageLiveData(
    private val documentReference: CollectionReference, private val familiar: Familiar
) : LiveData<List<Message>>(), EventListener<QuerySnapshot> {

    private var snapshotListener: ListenerRegistration? = null

    override fun onActive() {
        super.onActive()
        snapshotListener = documentReference
            .whereEqualTo("getes", familiar.emailCreator+familiar.emailFamiliar)
            .addSnapshotListener(this)
    }

    override fun onInactive() {
        super.onInactive()
        snapshotListener?.remove()
    }

    override fun onEvent(result: QuerySnapshot?, error: FirebaseFirestoreException?) {
        val messageList = mutableListOf<Message>()

        result?.forEach { documentSnapshot ->
            messageList.add(documentSnapshot.toObject(FirebaseMessage::class.java).toMessage)
        }
        value = messageList.toList()
    }
}