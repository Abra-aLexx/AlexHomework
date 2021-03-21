package com.abra.homework_10.data.repositories

import com.abra.homework_10.presentation.retrofit.RetrofitInitialization
import com.abra.homework_10.presentation.retrofit.WeatherApi
import com.abra.homework_10.presentation.retrofit.WeatherRootObject
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class RequestRepository {
    private val retrofit = RetrofitInitialization.getInstance()

    fun createRequest(cityName: String): Single<WeatherRootObject> =
            retrofit.create(WeatherApi::class.java).getWhetherForFiveDays(cityName)
                    .subscribeOn(Schedulers.io())
}