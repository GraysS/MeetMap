package org.meetmap.data.source.implementations

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import org.meetmap.common.*
import org.meetmap.common.awaitTaskCompletable
import org.meetmap.common.toUser
import org.meetmap.data.model.domain.Message
import org.meetmap.data.model.domain.User
import org.meetmap.data.source.repository.IMessageRepository

private const val COLLECTION_MESSAGES_NAME = "messages"

class FirebaseMessageRepoImpl(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val remote: FirebaseFirestore = FirebaseFirestore.getInstance()
) : IMessageRepository {

    override fun getMessages() : Result<Exception, CollectionReference> {
        val user = getActiveUser() ?: User("","")
        return getRemoteMessages(user)
    }

    override suspend fun deleteMessage(message: Message) : Result<Exception, Unit> {
        val user = getActiveUser() ?: User("","")
        return deleteRemoteFamiliar(message.copy(emailMyUser = user.email))
    }

    override suspend fun updateMessage(message: Message) : Result<Exception, Unit> {
        val user = getActiveUser() ?: User("","")
        return updateRemoteFamiliar(message.copy(emailMyUser = user.email))
    }

    private fun getActiveUser(): User? {
        return firebaseAuth.currentUser?.toUser
    }

    private fun getRemoteMessages(user: User): Result<Exception, CollectionReference> {
        return try {
            if(user.isEmpty)
                throw Exception("Not Authorization")

            val result = remote.collection(COLLECTION_MESSAGES_NAME)
            Result.build { result }
        } catch (exception: Exception) {
            Result.build { throw exception }
        }
    }


    private suspend fun deleteRemoteFamiliar(message: Message) : Result<Exception, Unit> {
        return try {
            awaitTaskCompletable(
                remote.collection(COLLECTION_MESSAGES_NAME)
                    .document(message.creationDate.toString() + message.emailFamiliar + message.emailMyUser)
                    .delete()
            )
            Result.build { Unit }
        } catch (exception: Exception) {
            Result.build { throw exception }
        }
    }

    private suspend fun updateRemoteFamiliar(message: Message) : Result<Exception, Unit> {
        return try {
            awaitTaskCompletable(
                remote.collection(COLLECTION_MESSAGES_NAME)
                    .document(message.creationDate.toString() + message.emailFamiliar + message.emailMyUser)
                    .set(message.toFirebaseMessageOne)
            )

            awaitTaskCompletable(
                remote.collection(COLLECTION_MESSAGES_NAME)
                    .document(message.creationDate.toString()  + message.emailMyUser + message.emailFamiliar)
                    .set(message.toFirebaseMessageTwo)
            )

            Result.build { Unit }
        } catch (exception: Exception) {
            Result.build { throw exception }
        }
    }
}