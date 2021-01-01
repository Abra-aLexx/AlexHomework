package com.abra.homework_5

import androidx.room.*

@Dao
interface CarInfoDAO{
    @Query("SELECT * FROM cars_info")
    fun getAll(): List<CarInfo>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun add(entity: CarInfo)
    @Query("DELETE FROM cars_info")
    fun deleteAll()
    @Update
    fun update(carInfo: CarInfo)
}