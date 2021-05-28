package org.meetmap.data.source.implementations

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import org.meetmap.common.*
import org.meetmap.data.model.domain.Point
import org.meetmap.data.model.domain.User
import org.meetmap.data.model.network.FirebasePoint
import org.meetmap.data.source.repository.IPointRepository
import timber.log.Timber

private const val COLLECTION_POINTS_NAME = "points"

class FirebasePointRepoImpl(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val remote: FirebaseFirestore = FirebaseFirestore.getInstance()
)  : IPointRepository {

    override suspend fun getPoints() : Result<Exception, List<Point>> {
        val user = getActiveUser() ?: User("","")
        return getRemotePoints(user)
    }

    override suspend fun deletePoint(point: Point) : Result<Exception, Unit> {
        val user = getActiveUser() ?: User("","")
        return deleteRemotePoint(point.copy(creator = user))
    }

    override suspend fun updatePoint(point: Point) : Result<Exception, Unit> {
        val user = getActiveUser() ?: User("","")
        return updateRemotePoint(point.copy(creator = user))
    }

    private fun getActiveUser(): User? {
        return firebaseAuth.currentUser?.toUser
    }

    private fun resultToPointList(result: QuerySnapshot?): Result<Exception, List<Point>> {
        val noteList = mutableListOf<Point>()

        result?.forEach { documentSnapshot ->
            noteList.add(documentSnapshot.toObject(FirebasePoint::class.java).toPoint)
        }

        return Result.build {
            noteList
        }
    }

    private suspend fun getRemotePoints(user: User): Result<Exception, List<Point>> {
        return try {
            if(user.isEmpty)
                throw Exception("Not Authorization")

            val task = awaitTaskResult(
                    remote.collection(COLLECTION_POINTS_NAME)
                            .whereEqualTo("creator", user.uid)
                            .get()
            )

            resultToPointList(task)
        } catch (exception: Exception) {
            Result.build { throw exception }
        }
    }


    private suspend fun deleteRemotePoint(point: Point): Result<Exception, Unit> = Result.build {
        if((point.creator?.isEmpty ?: User("","")) as Boolean)
            throw Exception("Not Creator User")


        awaitTaskCompletable(
            remote.collection(COLLECTION_POINTS_NAME)
                .document(point.creationDate.toString() + point.creator?.uid)
                .delete()
        )
    }

    private suspend fun updateRemotePoint(point: Point) : Result<Exception, Unit> {
        return try {
            if((point.creator?.isEmpty ?: User("","")) as Boolean) {
                Timber.d("FUCKS")
                throw Exception("Not Creator User")
            }

             awaitTaskCompletable(
                 remote.collection(COLLECTION_POINTS_NAME)
                     .document(point.creationDate.toString() + point.creator?.uid)
                     .set(point.toFirebasePoint)
             )

            Result.build { Unit }
        } catch (exception: Exception) {
            Result.build { throw exception }
        }
    }

}

