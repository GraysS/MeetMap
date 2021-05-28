package org.meetmap.common

import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import org.meetmap.data.model.domain.*
import org.meetmap.data.model.network.FirebaseFamiliar
import org.meetmap.data.model.network.FirebaseMessage
import org.meetmap.data.model.network.FirebasePoint
import org.meetmap.data.model.network.FirebasePointsFamiliar
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.properties.ReadOnlyProperty

internal suspend fun <T> awaitTaskResult(task: Task<T>): T = suspendCoroutine { continuation ->
    task.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            continuation.resume(task.result!!)
        } else {
            continuation.resumeWithException(task.exception!!)
        }
    }
}

//Wraps Firebase/GMS calls
internal suspend fun <T> awaitTaskCompletable(task: Task<T>): Unit = suspendCoroutine { continuation ->
    task.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            continuation.resume(Unit)
        } else {
            continuation.resumeWithException(task.exception!!)
        }
    }
}

inline fun <reified R : ActivityResultLauncher<String>> Fragment.requestPermission(
    permission: String,
    noinline granted: (permission: String) -> Unit = {},
    noinline denied: (permission: String) -> Unit = {},
    noinline explained: (permission: String) -> Unit = {}
): ReadOnlyProperty<Fragment, R> = PermissionResultDelegate(this, permission, granted, denied, explained)

internal val FirebaseUser.toUser: User
    get() = User(
            uid = this.uid,
            email = this.email ?: ""
    )

internal val FirebasePoint.toPoint: Point
    get() = Point(
        this.creationDate ?: 0,
        User(this.creator ?: "",""),
        this.title ?: "",
        this.description ?: "",
        this.latitude ?: 0.0,
        this.longitude ?: 0.0
    )

internal val FirebaseFamiliar.toFamiliar: Familiar
    get() = Familiar(
        this.creationDate ?: 0,
        this.emailFamiliar ?: "",
        this.nickname ?: "",
      this.emailCreator ?: ""
    )

internal val FirebaseMessage.toMessage: Message
    get() = Message(this.creationDate ?: 0,
        this.emailFamiliar ?: "",
        this.emailMyUser ?: "",
             this.message ?: ""
            )

internal val FirebasePointsFamiliar.toPointsFamiliar: PointsFamiliar
    get() = PointsFamiliar(
        this.creationDate ?: 0,
        this.emailFamiliar ?: "",
        this.emailMyUser ?: "",
        this.latitude ?: 0.0,
        this.longitude ?: 0.0
    )


internal val Point.toFirebasePoint: FirebasePoint
    get() = FirebasePoint(
        this.creationDate,
        this.creator?.uid,
        this.title,
        this.description,
        this.latitude,
        this.longitude
    )

internal val Familiar.toFirebaseFamiliarOne: FirebaseFamiliar
    get() = FirebaseFamiliar(
        this.creationDate,
        this.emailFamiliar,
        this.nickname,
        this.emailCreator
    )

internal val Familiar.toFirebaseFamiliarTwo: FirebaseFamiliar
    get() = FirebaseFamiliar(
        this.creationDate,
        this.emailCreator,
        this.nickname,
        this.emailFamiliar
    )

internal val User.toFirebaseUser: org.meetmap.data.model.network.FirebaseUser
    get() = org.meetmap.data.model.network.FirebaseUser(
        this.uid,
        this.email
    )

internal val Message.toFirebaseMessageOne: FirebaseMessage
    get() = FirebaseMessage(
            this.creationDate,
            this.emailFamiliar,
            this.emailMyUser,
            this.message,
            emailMyUser+emailFamiliar
        )

internal val Message.toFirebaseMessageTwo: FirebaseMessage
    get() = FirebaseMessage(
        this.creationDate,
        this.emailFamiliar,
        this.emailMyUser,
        this.message,
        emailFamiliar+emailMyUser
    )

internal val PointsFamiliar.toFirebasePointsFamiliarOne: FirebasePointsFamiliar
    get() = FirebasePointsFamiliar(
        this.creationDate,
        this.emailFamiliar,
        this.emailMyUser,
        this.latitude,
        this.longitude,
        emailMyUser+emailFamiliar
    )

internal val PointsFamiliar.toFirebasePointsFamiliarTwo: FirebasePointsFamiliar
    get() = FirebasePointsFamiliar(
        this.creationDate,
        this.emailFamiliar,
        this.emailMyUser,
        this.latitude,
        this.longitude,
        emailFamiliar+emailMyUser
    )

/*
internal fun List<RoomNote>.toNoteListFromRoomNote(): List<Note> = this.flatMap {
    listOf(it.toNote)
}
*/

/*
internal fun String.toEditable(): Editable =
    Editable.Factory.getInstance().newEditable(this)
*/

/*
internal val Note.safeGetUid: String
    get() = this.creator?.uid ?: ""
*/
