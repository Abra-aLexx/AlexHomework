package com.abra.homework_5.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.abra.homework_5.data.CarInfo
import com.abra.homework_5.data.WorkInfo

@Database(entities = [CarInfo::class, WorkInfo::class], version = 1, exportSchema = false)
abstract class DataBaseCarInfo : RoomDatabase() {
    abstract fun getCarInfoDAO(): CarInfoDAO
    abstract fun getWorkInfoDAO(): WorkInfoDAO

    companion object {
        private var INSTANCE: DataBaseCarInfo? = null
        fun getDataBase(context: Context): DataBaseCarInfo {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DataBaseCarInfo::class.java,
                    "database")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            INSTANCE = instance
            return instance
        }
    }
}