package com.abra.homework_8_2.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.abra.homework_8_2.data.WorkInfo

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