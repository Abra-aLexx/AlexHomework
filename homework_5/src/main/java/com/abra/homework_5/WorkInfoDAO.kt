package com.abra.homework_5

import androidx.room.*

@Dao
interface WorkInfoDAO {
    @Query("SELECT * FROM works_info WHERE carInfoId = :carInfoId")
    fun getAllWorksForCar(carInfoId: Int): List<WorkInfo>
    @Insert
    fun addWorkInfo(entity: WorkInfo)
    @Query("DELETE FROM cars_info")
    fun deleteAll()
    @Update
    fun update(workInfo: WorkInfo)
    @Delete
    fun delete(workInfo: WorkInfo)
}