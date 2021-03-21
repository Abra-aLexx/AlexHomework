package com.abra.homework_10.data.repositories

import android.content.Context
import com.abra.homework_10.presentation.database.CityData
import com.abra.homework_10.presentation.database.DatabaseCities
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DatabaseRepository(private val scope: CoroutineScope) {
    companion object {
        private lateinit var database: DatabaseCities
        fun initDatabase(context: Context) {
            database = DatabaseCities.getDatabase(context)
        }
    }

    suspend fun addCity(cityData: CityData) {
        withContext(scope.coroutineContext + Dispatchers.IO) {
            database.getCityDataDAO().addCityData(cityData)
        }
    }

    suspend fun getAllList() =
            withContext(scope.coroutineContext + Dispatchers.IO) {
                database.getCityDataDAO().getAllCities()
            }

    suspend fun getCityById(id: Int) =
            withContext(scope.coroutineContext + Dispatchers.IO) {
                database.getCityDataDAO().getCityDataById(id)
            }
}