package com.abra.homework_10.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.abra.homework_10.R
import com.abra.homework_10.presentation.database.CityData
import com.abra.homework_10.databinding.FragmentAddCityBinding
import com.abra.homework_10.observer.ManagerFactory
import com.abra.homework_10.presentation.viewmodel.DatabaseViewModel
import com.abra.homework_10.presentation.viewmodel.DatabaseViewModelFactory
import com.abra.homework_10.presentation.viewmodel.WeatherForecastViewModel
import com.abra.homework_10.presentation.viewmodel.WeatherForecastViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class AddCityFragment : DialogFragment() {
    private lateinit var viewModel: WeatherForecastViewModel
    private lateinit var databaseViewModel: DatabaseViewModel
    private var binding: FragmentAddCityBinding? = null
    private val manager = ManagerFactory.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddCityBinding.bind(inflater.inflate(R.layout.fragment_add_city, container, false))
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, WeatherForecastViewModelFactory())
                .get(WeatherForecastViewModel::class.java)
        databaseViewModel = ViewModelProvider(this, DatabaseViewModelFactory(CoroutineScope(Dispatchers.Main + Job())))
                .get(DatabaseViewModel::class.java)
        setOkButtonListener()
        setCancelButtonListener()
    }

    private fun setOkButtonListener() {
        binding?.run {
            buttonOk.setOnClickListener {
                val cityName = etCityName.text.toString()
                if (cityName.isNotEmpty()) {
                    loadData(cityName)
                } else {
                    Snackbar.make(it, "Write city or country name!", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadData(cityName: String) {
        viewModel.weatherRootObjectLiveData.observe(viewLifecycleOwner, { data ->
            if (data.city.name.isNotEmpty()) {
                prepareLoadedData(data.city.name, data.city.country)
            } else {
                Snackbar.make(view as View, "No such city or country!", Snackbar.LENGTH_SHORT).show()
            }
        })
        viewModel.requestData(cityName)
    }

    private fun prepareLoadedData(cityName: String, country: String) {
        databaseViewModel.listLiveData.observe(viewLifecycleOwner,
                { list ->
                    /*
                    * Странный момент, почему то при добавлении в ViewModel после возвращения
                    * на фрагмент с выбором города не отображается добавленный город, хотя в 9-ой
                    * домашке без архитектуры всё работало нормально, однако я нашел такой способ
                    * решения проблемы
                    *  */
                    val arrayListOfCities = ArrayList(list)
                    val cityData: CityData
                    if (arrayListOfCities.size != 0) {
                        cityData = CityData(cityName, country)
                                .also {
                                    it.id = arrayListOfCities[arrayListOfCities.size - 1].id + 1
                                }
                    } else {
                        cityData = CityData(cityName, country)
                                .also {
                                    it.id = 1
                                }
                    }
                    arrayListOfCities.add(cityData)
                    manager.setList(arrayListOfCities)
                    databaseViewModel.addCity(cityData)
                    dismiss()
                })
        databaseViewModel.requestAllList()
    }

    private fun setCancelButtonListener() {
        binding?.buttonCancel?.setOnClickListener {
            dismiss()
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.closeOperations()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}