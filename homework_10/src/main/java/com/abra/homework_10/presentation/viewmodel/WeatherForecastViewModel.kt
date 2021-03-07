package com.abra.homework_10.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.abra.homework_10.domain.WeatherForecastUseCase
import com.abra.homework_10.presentation.retrofit.City
import com.abra.homework_10.presentation.retrofit.WeatherRootObject
import io.reactivex.disposables.CompositeDisposable

class WeatherForecastViewModel : ViewModel() {
    private val disposableContainer: CompositeDisposable = CompositeDisposable()
    private val useCase = WeatherForecastUseCase()

    private val weatherRootObjectMutableLiveData = MutableLiveData<WeatherRootObject>()
    val weatherRootObjectLiveData: LiveData<WeatherRootObject> = weatherRootObjectMutableLiveData

    fun requestData(cityName: String) {
        useCase.getWhetherForFiveDays(cityName)
                .subscribe(
                        { data -> weatherRootObjectMutableLiveData.value = data },
                        { weatherRootObjectMutableLiveData.value = WeatherRootObject(mutableListOf(), City("", "")) }
                ).also { disposableContainer.add(it) }
    }

    fun closeOperations() {
        disposableContainer.clear()
    }
}