package org.meetmap.data.source.repository

import org.meetmap.common.Result
import org.meetmap.data.model.domain.Point

interface IPointRepository {

    suspend fun getPoints() : Result<Exception, List<Point>>

    suspend fun deletePoint(point: Point) : Result<Exception, Unit>

    suspend fun updatePoint(point: Point) : Result<Exception, Unit>

}