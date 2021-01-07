package com.abra.homework_5.data

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "works_info")
class WorkInfo(@ColumnInfo val date: String,
               @ColumnInfo var workName: String,
               @ColumnInfo var description: String,
               @ColumnInfo var cost: String,
               @ColumnInfo var status: String,
               @ColumnInfo val carInfoId: Long) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    var id: Long? = null

    constructor(parcel: Parcel) : this(
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readLong()) {
        id = parcel.readLong()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(date)
        parcel.writeString(workName)
        parcel.writeString(description)
        parcel.writeString(cost)
        parcel.writeString(status)
        parcel.writeLong(carInfoId)
        parcel.writeLong(id ?: -1)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WorkInfo> {
        override fun createFromParcel(parcel: Parcel): WorkInfo {
            return WorkInfo(parcel)
        }

        override fun newArray(size: Int): Array<WorkInfo?> {
            return arrayOfNulls(size)
        }
    }
}