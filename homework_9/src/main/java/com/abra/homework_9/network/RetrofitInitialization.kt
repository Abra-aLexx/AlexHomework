package com.abra.homework_9.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInitialization {
    companion object{
        private val retrofit =  Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        fun getInstance(): Retrofit = retrofit
    }
}