package com.abra.homework_9.repositories

import com.abra.homework_9.network.RetrofitInitialization
import com.abra.homework_9.network.WeatherApi
import com.abra.homework_9.network.WeatherRootObject
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class RequestRepository {
    private val retrofit = RetrofitInitialization.getInstance()

    fun createRequest(cityName: String): Single<WeatherRootObject> =
            retrofit.create(WeatherApi::class.java).getWhetherForFiveDays(cityName)
                    .subscribeOn(Schedulers.io())
}