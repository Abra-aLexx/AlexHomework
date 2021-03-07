package com.abra.homework_10.domain

import com.abra.homework_10.data.repositories.DatabaseRepository
import com.abra.homework_10.presentation.database.CityData
import kotlinx.coroutines.CoroutineScope

class DatabaseUseCase(private val scope: CoroutineScope) {
    private val repository = DatabaseRepository(scope)
    suspend fun addCity(cityData: CityData) {
        repository.addCity(cityData)
    }

    suspend fun getAllList() = repository.getAllList()

    suspend fun getCityById(id: Int) = repository.getCityById(id)
}