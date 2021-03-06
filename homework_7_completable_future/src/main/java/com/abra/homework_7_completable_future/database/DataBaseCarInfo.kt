package com.abra.homework_7_completable_future.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.abra.homework_7_completable_future.data.CarInfo
import com.abra.homework_7_completable_future.data.WorkInfo

@Database(entities = [CarInfo::class, WorkInfo::class], version = 1, exportSchema = false)
abstract class DataBaseCarInfo : RoomDatabase() {
    abstract fun getCarInfoDAO(): CarInfoDAO
    abstract fun getWorkInfoDAO(): WorkInfoDAO

    companion object {
        private var INSTANCE: DataBaseCarInfo? = null
        fun getDataBase(context: Context): DataBaseCarInfo {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        DataBaseCarInfo::class.java,
                        "database")
                        .build()
            }
            return INSTANCE as DataBaseCarInfo
        }
    }
}