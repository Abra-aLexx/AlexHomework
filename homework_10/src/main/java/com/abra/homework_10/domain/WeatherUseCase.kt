package com.abra.homework_10.domain

import com.abra.homework_10.presentation.retrofit.WeatherRootObject
import io.reactivex.Single

interface WeatherUseCase {
    fun getWhetherForFiveDays(cityName: String): Single<WeatherRootObject>
}