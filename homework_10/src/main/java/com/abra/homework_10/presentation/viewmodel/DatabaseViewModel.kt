package com.abra.homework_10.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.abra.homework_10.domain.DatabaseUseCase
import com.abra.homework_10.presentation.database.CityData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DatabaseViewModel(private val scope: CoroutineScope) : ViewModel() {
    private val useCase = DatabaseUseCase(scope)
    private val mutableListLiveData = MutableLiveData<List<CityData>>()
    val listLiveData: LiveData<List<CityData>> = mutableListLiveData
    private val mutableCityDataLiveData = MutableLiveData<CityData>()
    val cityDataLiveData: LiveData<CityData> = mutableCityDataLiveData
    fun addCity(cityData: CityData) {
        scope.launch {
            useCase.addCity(cityData)
        }
    }

    fun requestAllList() {
        scope.launch {
            mutableListLiveData.value = useCase.getAllList()
        }
    }

    fun requestCityById(id: Int) {
        scope.launch {
            mutableCityDataLiveData.value = useCase.getCityById(id)
        }
    }
}