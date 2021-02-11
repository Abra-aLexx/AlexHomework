package com.abra.homework_9.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.abra.homework_9.R
import com.abra.homework_9.databinding.FragmentCurrentRegionBinding
import com.abra.homework_9.functions.setCurrentDate
import com.abra.homework_9.network.*
import com.abra.homework_9.repositories.DatabaseRepository
import com.abra.homework_9.repositories.RequestRepository
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class FragmentWeatherForecast : Fragment(R.layout.fragment_current_region) {
    private val disposableContainer: CompositeDisposable = CompositeDisposable()
    private lateinit var fragmentLoader: FragmentLoader
    private val fragmentScope = CoroutineScope(Dispatchers.Main + Job())
    private lateinit var requestRepository: RequestRepository
    private lateinit var repository: DatabaseRepository
    private var binding: FragmentCurrentRegionBinding? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCurrentRegionBinding.bind(inflater.inflate(R.layout.fragment_current_region, container, false))
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.visibility = View.INVISIBLE
        fragmentLoader = requireActivity() as FragmentLoader
        repository = DatabaseRepository(fragmentScope)
        requestRepository = RequestRepository()
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
        fragmentScope.launch {
            loadData(repository.getCityById(id).name)
        }
    }

    private fun loadData(cityName: String) {
        requestRepository.createRequest(cityName)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { data -> setWeatherForecast(data) },
                        { Snackbar.make(view as View, "Error occurred while loading data", Snackbar.LENGTH_SHORT).show() }
                ).also { disposableContainer.add(it) }

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
        disposableContainer.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}