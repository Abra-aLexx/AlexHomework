package com.abra.homework_5

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cars_info")
class CarInfo(@PrimaryKey @ColumnInfo var id: Int,
              @ColumnInfo val pathToPicture: String?,
              @ColumnInfo var name: String?,
              @ColumnInfo var producer: String?,
              @ColumnInfo var model: String?):Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())
    constructor(id: Int, carInfo: CarInfo):this(id,carInfo.pathToPicture, carInfo.name, carInfo.producer, carInfo.model)
    override fun describeContents(): Int {
        return 0
    }
    override fun writeToParcel(p0: Parcel, p1: Int) {
        p0.writeInt(id)
        p0.writeString(pathToPicture)
        p0.writeString(name)
        p0.writeString(producer)
        p0.writeString(model)
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

