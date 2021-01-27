package com.abra.homework_7_executor_service.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.OnConflictStrategy
import com.abra.homework_7_executor_service.data.CarInfo

@Dao
interface CarInfoDAO {
    @Query("SELECT * FROM cars_info")
    fun getAll(): List<CarInfo>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun add(entity: CarInfo)

    @Update
    fun update(carInfo: CarInfo)
}