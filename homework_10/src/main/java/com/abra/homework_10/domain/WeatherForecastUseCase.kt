package com.abra.homework_10.domain

import com.abra.homework_10.data.repositories.RequestRepository
import io.reactivex.android.schedulers.AndroidSchedulers

class WeatherForecastUseCase : WeatherUseCase {
    private val repository: RequestRepository = RequestRepository()
    override fun getWhetherForFiveDays(cityName: String) =
            repository.createRequest(cityName)
                    .observeOn(AndroidSchedulers.mainThread())
}