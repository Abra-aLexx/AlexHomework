package com.abra.homework_9.network

import com.google.gson.annotations.SerializedName

data class WeatherRootObject(
        val list: MutableList<WeatherForecast>,
        val city: City,
)

data class City(
        val name: String,
        val country: String
)

data class WeatherForecast(
        val main: MainDescription,
        val weather: MutableList<WeatherDescription>,
        @SerializedName("dt_txt") val dtTxt: String
)

data class MainDescription(
        val temp: Double
)

data class WeatherDescription(
        val main: String,
        val icon: String,
)
