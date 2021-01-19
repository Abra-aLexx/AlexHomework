package com.abra.homework_7_rx_java.database

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.abra.homework_7_rx_java.data.WorkInfo

@Dao
interface WorkInfoDAO {
    @Query("SELECT * FROM works_info WHERE carInfoId = :carInfoId")
    fun getAllWorksForCar(carInfoId: Long): List<WorkInfo>

    @Query("SELECT * FROM works_info")
    fun selectAll(): Cursor?

    @Insert
    fun addWork(entity: WorkInfo)

    @Update
    fun update(workInfo: WorkInfo)

    @Delete
    fun delete(workInfo: WorkInfo)
}