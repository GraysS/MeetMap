package org.meetmap.data.source.repository

import org.meetmap.data.model.domain.User
import org.meetmap.common.Result

interface IUserRepository {
    suspend fun getCurrentUser(): Result<Exception, User?>

    suspend fun signOutCurrentUser(): Result<Exception, Unit>

    suspend fun createUserWithEmailAndPassword(email: String, password: String): Result<Exception, Unit>

    suspend fun signInWihEmailAndPassword(email: String,password: String): Result<Exception, Unit>
}