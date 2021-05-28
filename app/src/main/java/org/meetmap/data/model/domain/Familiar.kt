package org.meetmap.data.model.domain

import android.os.Parcel
import android.os.Parcelable

data class Familiar(val creationDate: Long,
                    val emailFamiliar: String,
                    val nickname: String,
                    val emailCreator: String
                    ) : Parcelable {


    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(creationDate)
        parcel.writeString(emailFamiliar)
        parcel.writeString(nickname)
        parcel.writeString(emailCreator)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Familiar> {
        override fun createFromParcel(parcel: Parcel): Familiar {
            return Familiar(parcel)
        }

        override fun newArray(size: Int): Array<Familiar?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "familiar: $emailFamiliar emailCreator: $emailCreator nickname: $nickname"
    }

}
