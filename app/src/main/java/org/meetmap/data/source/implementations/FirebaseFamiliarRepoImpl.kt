package org.meetmap.data.source.implementations

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import org.meetmap.common.*
import org.meetmap.common.awaitTaskCompletable
import org.meetmap.common.awaitTaskResult
import org.meetmap.common.toUser
import org.meetmap.data.model.domain.Familiar
import org.meetmap.data.model.domain.User
import org.meetmap.data.model.network.FirebaseFamiliar
import org.meetmap.data.source.repository.IFamiliarRepository

private const val COLLECTION_FAMILIARS_NAME = "familiars"

class FirebaseFamiliarRepoImpl(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val remote: FirebaseFirestore = FirebaseFirestore.getInstance()
) : IFamiliarRepository {

    override suspend fun getFamiliars() : Result<Exception, List<Familiar>> {
        val user = getActiveUser() ?: User("","")
        return getRemoteFamiliars(user)
    }

    override suspend fun deleteFamiliar(familiar: Familiar) : Result<Exception, Unit> {
        val user = getActiveUser() ?: User("","")
        return deleteRemoteFamiliar(familiar.copy(emailCreator = user.email))
    }

    override suspend fun updateFamiliar(familiar: Familiar) : Result<Exception, Unit> {
        val user = getActiveUser() ?: User("","")
        return updateRemoteFamiliar(familiar.copy(emailCreator = user.email))
    }

    private fun getActiveUser(): User? {
        return firebaseAuth.currentUser?.toUser
    }

    private fun resultToFamiliarList(result: QuerySnapshot?): Result<Exception, List<Familiar>> {
        val familiarList = mutableListOf<Familiar>()

        result?.forEach { documentSnapshot ->
            familiarList.add(documentSnapshot.toObject(FirebaseFamiliar::class.java).toFamiliar)
        }

        return Result.build {
            familiarList
        }
    }

    private suspend fun getRemoteFamiliars(user: User): Result<Exception, List<Familiar>> {
        return try {
            if(user.isEmpty)
                throw Exception("Not Authorization")

            val task = awaitTaskResult(
                remote.collection(COLLECTION_FAMILIARS_NAME)
                    .whereEqualTo("emailCreator", user.email)
                    .get()
            )

            resultToFamiliarList(task)
        } catch (exception: Exception) {
            Result.build { throw exception }
        }
    }


    private suspend fun deleteRemoteFamiliar(familiar: Familiar) : Result<Exception, Unit> {
        return try {
            awaitTaskCompletable(
                remote.collection(COLLECTION_FAMILIARS_NAME)
                    .document(familiar.creationDate.toString() + familiar.emailCreator)
                    .delete()
            )
            Result.build { Unit }
        } catch (exception: Exception) {
            Result.build { throw exception }
        }
    }

    private suspend fun updateRemoteFamiliar(familiar: Familiar) : Result<Exception, Unit> {
        return try {
            awaitTaskCompletable(
                remote.collection(COLLECTION_FAMILIARS_NAME)
                    .document(familiar.creationDate.toString() + familiar.emailCreator)
                    .set(familiar.toFirebaseFamiliarOne)
            )

            awaitTaskCompletable(
                remote.collection(COLLECTION_FAMILIARS_NAME)
                    .document(familiar.creationDate.toString() + familiar.emailFamiliar)
                    .set(familiar.toFirebaseFamiliarTwo)
            )
            Result.build { Unit }
        } catch (exception: Exception) {
            Result.build { throw exception }
        }
    }

}