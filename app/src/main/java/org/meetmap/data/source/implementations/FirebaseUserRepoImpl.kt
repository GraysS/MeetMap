package org.meetmap.data.source.implementations

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.meetmap.common.Result
import org.meetmap.common.awaitTaskCompletable
import org.meetmap.common.toFirebaseUser
import org.meetmap.data.model.domain.User
import org.meetmap.data.source.repository.IUserRepository

private const val COLLECTION_USERS_NAME = "users"

class FirebaseUserRepoImpl(private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
                           private val remote: FirebaseFirestore = FirebaseFirestore.getInstance()) : IUserRepository {

    override suspend fun signOutCurrentUser(): Result<Exception, Unit> {
        return try {
            Result.build { auth.signOut() }
        } catch (exception: Exception) {
            Result.build { throw exception }
        }
    }

    override suspend fun createUserWithEmailAndPassword(email: String, password: String): Result<Exception, Unit> = withContext(Dispatchers.IO) {
        try {
            val credential = auth.createUserWithEmailAndPassword(email,password)
            awaitTaskCompletable(credential)

            val resultUser = getCurrentUser()
            if(resultUser is Result.Value) {
                awaitTaskCompletable(remote.collection(COLLECTION_USERS_NAME)
                    .document(email)
                    .set(resultUser.value!!.toFirebaseUser)
                )
            }
            Result.build { Unit }
        } catch (exception: Exception) {
            Result.build { throw exception }
        }
    }

    override suspend fun signInWihEmailAndPassword(email: String, password: String): Result<Exception, Unit> = withContext(Dispatchers.IO) {
        try {
            val credential = auth.signInWithEmailAndPassword(email,password)
            awaitTaskCompletable(credential)
            Result.build { Unit }
        } catch (exception: Exception) {
            Result.build { throw exception }
        }
    }

    override suspend fun getCurrentUser(): Result<Exception, User?> {
        val firebaseUser = auth.currentUser

        return if (firebaseUser == null) {
            Result.build { null }
        } else {
            Result.build {
                User(
                        firebaseUser.uid,
                        firebaseUser.email ?: ""
                )
            }
        }
    }
}