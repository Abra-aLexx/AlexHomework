package com.abra.homework_5

import androidx.room.*

@Dao
interface WorkInfoDAO {
    @Query("SELECT * FROM works_info WHERE carInfoId = :carInfoId")
    fun getAllWorksForCar(carInfoId: Long): List<WorkInfo>
    @Insert
    fun addWork(entity: WorkInfo)
    @Update
    fun update(workInfo: WorkInfo)
    @Delete
    fun delete(workInfo: WorkInfo)
}