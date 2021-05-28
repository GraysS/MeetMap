package org.meetmap.data.source.repository

import com.google.firebase.firestore.CollectionReference
import org.meetmap.common.Result
import org.meetmap.data.model.domain.PointsFamiliar

interface IPointsFamiliarRepository  {

    suspend fun getPoints() : Result<Exception, CollectionReference>

    suspend fun updateMessage(pointsFamiliar: PointsFamiliar) : Result<Exception, Unit>

}