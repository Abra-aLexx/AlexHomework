package com.abra.homework_10.presentation.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
class CityData(@ColumnInfo val name: String,
               @ColumnInfo val country: String) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}