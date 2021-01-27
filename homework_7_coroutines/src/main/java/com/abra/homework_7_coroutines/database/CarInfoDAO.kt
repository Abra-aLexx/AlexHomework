package com.abra.homework_7_coroutines.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.OnConflictStrategy
import com.abra.homework_7_coroutines.data.CarInfo

@Dao
interface CarInfoDAO {
    @Query("SELECT * FROM cars_info")
    suspend fun getAll(): List<CarInfo>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun add(entity: CarInfo)

    @Update
    suspend fun update(carInfo: CarInfo)
}