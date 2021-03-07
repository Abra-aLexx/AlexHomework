package com.abra.homework_10.presentation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.abra.homework_10.R
import com.abra.homework_10.databinding.FragmentCurrentRegionBinding
import com.abra.homework_10.functions.setCurrentDate
import com.abra.homework_10.presentation.retrofit.*
import com.abra.homework_10.presentation.viewmodel.DatabaseViewModel
import com.abra.homework_10.presentation.viewmodel.DatabaseViewModelFactory
import com.abra.homework_10.presentation.viewmodel.WeatherForecastViewModel
import com.abra.homework_10.presentation.viewmodel.WeatherForecastViewModelFactory
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.math.roundToInt

class FragmentWeatherForecast : Fragment(R.layout.fragment_current_region) {
    private lateinit var databaseViewModel: DatabaseViewModel
    private lateinit var viewModel: WeatherForecastViewModel
    private lateinit var fragmentLoader: FragmentLoader
    private var binding: FragmentCurrentRegionBinding? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCurrentRegionBinding.bind(inflater.inflate(R.layout.fragment_current_region, container, false))
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.visibility = View.INVISIBLE
        viewModel = ViewModelProvider(this, WeatherForecastViewModelFactory())
                .get(WeatherForecastViewModel::class.java)
        databaseViewModel = ViewModelProvider(this, DatabaseViewModelFactory(CoroutineScope(Dispatchers.Main + Job())))
                .get(DatabaseViewModel::class.java)
        fragmentLoader = requireActivity() as FragmentLoader
        setButtonListener()
        getData()
    }

    private fun setButtonListener() {
        binding?.run {
            ibChooseCity.setOnClickListener {
                fragmentLoader.loadFragment(ChooseCityFragment::class.java,
                        bundleOf("isBackFromForecast" to true))
            }
        }
    }

    private fun getData() {
        val id = requireArguments().getInt("id", 0)
        databaseViewModel.cityDataLiveData.observe(viewLifecycleOwner,
                { data ->
                    loadData(data.name)
                })
        databaseViewModel.requestCityById(id)
    }

    private fun loadData(cityName: String) {
        viewModel.weatherRootObjectLiveData.observe(viewLifecycleOwner, { data ->
            if (data.city.name.isNotEmpty()) {
                setWeatherForecast(data)
            } else {
                Snackbar.make(view as View, "Error occurred while loading data", Snackbar.LENGTH_SHORT).show()
            }
        })
        viewModel.requestData(cityName)
    }

    private fun setWeatherForecast(weatherRootObject: WeatherRootObject) {
        binding?.run {
            with(weatherRootObject) {
                setCurrentWeatherForecast(list[0])
                setWeatherForecastForOtherDays(city, prepareDate(list), list[0])
            }
        }
        view?.visibility = View.VISIBLE
    }

    /**
     * Метод я использовал для установления данных для текущего дня отдельно от других дней,
     * так как были кое-какие нюансы с остальным прогнозом.
     * */
    private fun setCurrentWeatherForecast(currentForecast: WeatherForecast) {
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
                                               currentForecast: WeatherForecast) {
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

    override fun onStop() {
        super.onStop()
        viewModel.closeOperations()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}