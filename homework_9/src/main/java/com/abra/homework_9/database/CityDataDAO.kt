package com.abra.homework_9.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CityDataDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCityData(cityData: CityData)

    @Query("SELECT * FROM cities WHERE id = :id")
    suspend fun getCityDataById(id: Int): CityData

    @Query("SELECT * FROM cities")
    suspend fun getAllCities(): List<CityData>
}