package org.meetmap.data.source.repository

import org.meetmap.common.Result
import org.meetmap.data.model.domain.Familiar

interface IFamiliarRepository {

    suspend fun getFamiliars() : Result<Exception, List<Familiar>>

    suspend fun deleteFamiliar(familiar: Familiar) : Result<Exception, Unit>

    suspend fun updateFamiliar(familiar: Familiar) : Result<Exception, Unit>

}