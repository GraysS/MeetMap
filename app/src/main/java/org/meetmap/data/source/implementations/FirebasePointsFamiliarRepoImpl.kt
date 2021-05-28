package org.meetmap.data.source.implementations

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import org.meetmap.common.*
import org.meetmap.common.awaitTaskCompletable
import org.meetmap.common.toUser
import org.meetmap.data.model.domain.PointsFamiliar
import org.meetmap.data.model.domain.User
import org.meetmap.data.source.repository.IPointsFamiliarRepository

private const val COLLECTION_POINTS_FAMILIAR_NAME = "points_familiar"

class FirebasePointsFamiliarRepoImpl(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val remote: FirebaseFirestore = FirebaseFirestore.getInstance()
) : IPointsFamiliarRepository {

    override suspend fun getPoints() : Result<Exception, CollectionReference> {
        val user = getActiveUser() ?: User("","")
        return getRemotePoints(user)
    }

    override suspend fun updateMessage(pointsFamiliar: PointsFamiliar) : Result<Exception, Unit> {
        val user = getActiveUser() ?: User("","")
        return updateRemoteFamiliar(pointsFamiliar.copy(emailMyUser = user.email))
    }

    private fun getRemotePoints(user: User): Result<Exception, CollectionReference> {
        return try {
            if(user.isEmpty)
                throw Exception("Not Authorization")

            val task = remote.collection(COLLECTION_POINTS_FAMILIAR_NAME)
            Result.build { task }
        } catch (exception: Exception) {
            Result.build { throw exception }
        }
    }

    private fun getActiveUser(): User? {
        return firebaseAuth.currentUser?.toUser
    }

    private suspend fun updateRemoteFamiliar(pointsFamiliar: PointsFamiliar) : Result<Exception, Unit> {
        return try {
            awaitTaskCompletable(
                remote.collection(COLLECTION_POINTS_FAMILIAR_NAME)
                    .document(pointsFamiliar.creationDate.toString() + pointsFamiliar.emailFamiliar + pointsFamiliar.emailMyUser)
                    .set(pointsFamiliar.toFirebasePointsFamiliarOne)
            )

            awaitTaskCompletable(
                remote.collection(COLLECTION_POINTS_FAMILIAR_NAME)
                    .document(pointsFamiliar.creationDate.toString()  + pointsFamiliar.emailMyUser + pointsFamiliar.emailFamiliar)
                    .set(pointsFamiliar.toFirebasePointsFamiliarTwo)
            )

            Result.build { Unit }
        } catch (exception: Exception) {
            Result.build { throw exception }
        }
    }
}