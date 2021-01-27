package com.abra.homework_7_rx_java.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.OnConflictStrategy
import com.abra.homework_7_rx_java.data.CarInfo

@Dao
interface CarInfoDAO {
    @Query("SELECT * FROM cars_info")
    fun getAll(): List<CarInfo>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun add(entity: CarInfo)

    @Update
    fun update(carInfo: CarInfo)
}