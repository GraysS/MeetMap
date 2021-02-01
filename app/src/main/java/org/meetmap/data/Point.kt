package org.meetmap.data

import java.util.*

data class Point (var title: String = "",
                var description: String = "",
                var latitude: Double = 0.0,
                var longitude: Double = 0.0,
                var isFavorite: Boolean = false,
                var id: String = UUID.randomUUID().toString()) {

    val isOutsider
        get() = !isFavorite

    val isEmpty
        get() = title.isEmpty() || description.isEmpty()
}