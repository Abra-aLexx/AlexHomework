package com.abra.homework_8_1

import android.os.Parcel
import android.os.Parcelable

class ContactItem(val iconId: Int,
                  val name: String,
                  val info: String,
                  val typeInfo: String) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readString().toString())

    override fun describeContents() = 0

    override fun writeToParcel(p0: Parcel, p1: Int) {
        p0.writeInt(iconId)
        p0.writeString(name)
        p0.writeString(info)
        p0.writeString(typeInfo)
    }

    companion object CREATOR : Parcelable.Creator<ContactItem> {
        override fun createFromParcel(parcel: Parcel) = ContactItem(parcel)

        override fun newArray(size: Int): Array<ContactItem?> = arrayOfNulls(size)
    }
}