package org.meetmap.data.source.repository

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import org.meetmap.common.Result
import org.meetmap.data.model.domain.Familiar
import org.meetmap.data.model.domain.Message

interface IMessageRepository {

    fun getMessages() : Result<Exception, CollectionReference>

    suspend fun deleteMessage(message: Message) : Result<Exception, Unit>

    suspend fun updateMessage(message: Message) : Result<Exception, Unit>

}