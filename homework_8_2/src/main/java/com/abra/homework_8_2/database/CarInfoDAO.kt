package com.abra.homework_8_2.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.OnConflictStrategy
import com.abra.homework_8_2.data.CarInfo

@Dao
interface CarInfoDAO {
    @Query("SELECT * FROM cars_info")
    fun getAll(): List<CarInfo>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun add(entity: CarInfo)

    @Update
    fun update(carInfo: CarInfo)
}