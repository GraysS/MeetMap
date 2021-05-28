package org.meetmap.data.model.domain

data class Point(val creationDate: Long,
                 val creator: User?,
                val title: String,
                val description: String,
                val latitude: Double,
                val longitude: Double
                )
{

   /* val isEmpty
        get() = title.isEmpty() || description.isEmpty()*/
}