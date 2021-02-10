package com.abra.homework_9.network

import android.annotation.SuppressLint
import android.content.Context
import com.abra.homework_9.databinding.FragmentCurrentRegionBinding
import com.abra.homework_9.functions.setCurrentDate
import com.bumptech.glide.Glide
import kotlin.math.roundToInt

/**
 * В этот класс я вынес логику по установлению прогноза погоды на 5 дней
 * */
class DataLoader(private val weatherRootObject: WeatherRootObject) {
    fun setWhetherForecast(binding: FragmentCurrentRegionBinding?, context: Context) {
        binding?.run {
            with(weatherRootObject) {
                setCurrentWeatherForecast(list[0], this@run, context)
                setWeatherForecastForOtherDays(city, prepareDate(list), list[0], this@run, context)
            }
        }
    }

    /**
     * Метод я использовал для установления данных для текущего дня отдельно от других дней,
     * так как были кое-какие нюансы с остальным прогнозом.
     * */
    private fun setCurrentWeatherForecast(currentForecast: WeatherForecast, binding: FragmentCurrentRegionBinding?, context: Context) {
        binding?.run {
            Glide.with(context).load("https://openweathermap.org/img/w/${currentForecast.weather[0].icon}.png")
                    .into(ivCurrentWeather)
            tvCurrentTemperature.text = currentForecast.main.temp.roundToInt().toString()
            tvCurrentWeather.text = currentForecast.weather[0].main
            setCurrentDate(tvCurrentDate)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setWeatherForecastForOtherDays(city: City,
                                               filteredList: List<WeatherForecast>,
                                               currentForecast: WeatherForecast,
                                               binding: FragmentCurrentRegionBinding?,
                                               context: Context) {
        val list: MutableList<WeatherForecast> = mutableListOf()
        list.addAll(filteredList)
        /*
        * Это проверка нужна для отображения правильного прогноза, так как были случаи,
        * когда прогноз показывался через день, либо следущий день совпадал с текущим.
        * */
        if (currentForecast.dtTxt.substring(0, 10) == list[0].dtTxt.substring(0, 10)) {
            list.removeFirst()
        } else {
            list.removeLast()
        }
        binding?.run {
            tvCityName.text = "${city.name}, ${city.country}"
            // создал массивы для удобного установления данных во все View
            val textViewDateArray = arrayOf(tvDate1, tvDate2, tvDate3, tvDate4)
            val textViewDescriptionArray = arrayOf(tvWeather1, tvWeather2, tvWeather3, tvWeather4)
            val textViewDegreesArray = arrayOf(tvTemperature1, tvTemperature2, tvTemperature3, tvTemperature4)
            val imageViewArray = arrayOf(ivWeather1, ivWeather2, ivWeather3, ivWeather4)

            list.forEach {
                val index = list.indexOf(it)
                // таким образом я отформатировал дату как было показано в макете
                val date = it.dtTxt.substring(0, 10)
                        .split("-")
                val reversedDate = "${date[2]}.${date[1]}.${date[0]}"
                // здесь через массивы я устанавливаю все необходимые данные во все View
                textViewDateArray[index].text = reversedDate
                textViewDegreesArray[index].text = it.main.temp.roundToInt().toString()
                Glide.with(context).load("https://openweathermap.org/img/w/${it.weather[0].icon}.png")
                        .into(imageViewArray[index])
                textViewDescriptionArray[index].text = it.weather[0].main
            }
        }
    }

    /**
     * В этом методе фильтрую дату по 12:00:00(просто решил взять это время
     * так как оно среднее)
     * */
    private fun prepareDate(list: List<WeatherForecast>) = list.filter { it.dtTxt.contains("12:00:00") }
}