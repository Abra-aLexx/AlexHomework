package com.abra.homework_5

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*

@Entity(tableName = "works_info")
class WorkInfo(@PrimaryKey @ColumnInfo val id:Int,
               @ColumnInfo val date: String?,
               @ColumnInfo var workName: String?,
               @ColumnInfo var description: String?,
               @ColumnInfo var cost: String?,
               @ColumnInfo var status: String?,
               @ColumnInfo val carInfoId:Int): Parcelable{
    @Ignore
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt())
    constructor( date: String?,
                 workName: String?,
                 description: String?,
                 cost: String?,
                 status: String?):this(0,date,workName,description,cost,status,0)
    constructor( date: String?,
                 workName: String?,
                 description: String?,
                 cost: String?,
                 status: String?,
                 carInfoId:Int):this(0,date,workName,description,cost,status,carInfoId)
    constructor(id: Int,workInfo: WorkInfo,
                carInfoId: Int):this(id,
            workInfo.date,
            workInfo.workName,
            workInfo.description,
            workInfo.cost,
            workInfo.status,
            carInfoId)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(date)
        parcel.writeString(workName)
        parcel.writeString(description)
        parcel.writeString(cost)
        parcel.writeString(status)
        parcel.writeInt(carInfoId)

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