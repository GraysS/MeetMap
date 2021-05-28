package org.meetmap.data.model.network

data class FirebasePoint(val creationDate: Long? = 0,
                         val creator: String? = "",
                         val title: String? = "",
                         val description: String? = "",
                         val latitude: Double? = 0.0,
                         val longitude: Double? = 0.0
)