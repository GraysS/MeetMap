package org.meetmap.view.meet

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.*
import org.meetmap.common.toPointsFamiliar
import org.meetmap.data.model.domain.Familiar
import org.meetmap.data.model.domain.PointsFamiliar
import org.meetmap.data.model.network.FirebasePointsFamiliar

class PointsFamiliarLiveData(private val documentReference: CollectionReference,
                             private val familiar: Familiar
) : LiveData<List<PointsFamiliar>>(), EventListener<QuerySnapshot> {

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
        val messageList = mutableListOf<PointsFamiliar>()

        result?.forEach { documentSnapshot ->
            messageList.add(documentSnapshot.toObject(FirebasePointsFamiliar::class.java).toPointsFamiliar)
        }
        value = messageList.toList()
    }
}