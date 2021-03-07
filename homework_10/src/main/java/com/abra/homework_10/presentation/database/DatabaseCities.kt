package com.abra.homework_10.presentation.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CityData::class], version = 1, exportSchema = false)
abstract class DatabaseCities : RoomDatabase() {
    abstract fun getCityDataDAO(): CityDataDAO

    companion object {
        private var INSTANCE: DatabaseCities? = null
        fun getDatabase(context: Context): DatabaseCities {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context,
                        DatabaseCities::class.java,
                        "database")
                        .build()
            }
            return INSTANCE as DatabaseCities
        }
    }
}