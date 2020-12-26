package com.abra.homework_5

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable

class CarInfo(val carBitmap: Bitmap?, val name: String?, val producer: String?, val model: String?):Parcelable {
    constructor(parcel: Parcel?) : this(
            parcel?.readParcelable<Bitmap>(Bitmap::class.java.classLoader),
            parcel?.readString(),
            parcel?.readString(),
            parcel?.readString())

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.writeParcelable(carBitmap, 1)
        p0?.writeString(name)
        p0?.writeString(producer)
        p0?.writeString(model)
    }

    companion object CREATOR : Parcelable.Creator<CarInfo> {
        override fun createFromParcel(parcel: Parcel?): CarInfo {
            return CarInfo(parcel)
        }

        override fun newArray(size: Int): Array<CarInfo?> {
            return arrayOfNulls(size)
        }
    }
}

