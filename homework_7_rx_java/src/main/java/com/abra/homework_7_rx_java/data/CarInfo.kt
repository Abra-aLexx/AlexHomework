package com.abra.homework_7_rx_java.data

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cars_info")
class CarInfo(@ColumnInfo val pathToPicture: String,
              @ColumnInfo var name: String,
              @ColumnInfo var producer: String,
              @ColumnInfo var model: String) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    var id: Long = 0

    constructor(parcel: Parcel) : this(
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readString().toString()) {
        id = parcel.readLong()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(p0: Parcel, p1: Int) {
        p0.writeString(pathToPicture)
        p0.writeString(name)
        p0.writeString(producer)
        p0.writeString(model)
        p0.writeLong(id)
    }

    companion object CREATOR : Parcelable.Creator<CarInfo> {
        override fun createFromParcel(parcel: Parcel): CarInfo {
            return CarInfo(parcel)
        }

        override fun newArray(size: Int): Array<CarInfo?> {
            return arrayOfNulls(size)
        }
    }
}

