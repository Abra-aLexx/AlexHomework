package com.abra.homework_7_coroutines.database

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.abra.homework_7_coroutines.data.WorkInfo

@Dao
interface WorkInfoDAO {
    @Query("SELECT * FROM works_info WHERE carInfoId = :carInfoId")
    suspend fun getAllWorksForCar(carInfoId: Long): List<WorkInfo>

    @Insert
    suspend fun addWork(entity: WorkInfo)

    @Update
    suspend fun update(workInfo: WorkInfo)

    @Delete
    suspend fun delete(workInfo: WorkInfo)
}