package com.abra.homework_9.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/2.5/forecast?&units=metric&appid=4fe6a5d65137b731fd4be9933f17db2e")
    fun getWhetherForFiveDays(@Query("q") q: String): Call<WeatherRootObject>
}