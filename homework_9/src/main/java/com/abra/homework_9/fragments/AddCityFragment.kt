package com.abra.homework_9.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.abra.homework_9.R
import com.abra.homework_9.database.CityData
import com.abra.homework_9.databinding.FragmentAddCityBinding
import com.abra.homework_9.network.RetrofitInitialization
import com.abra.homework_9.network.WeatherApi
import com.abra.homework_9.network.WeatherRootObject
import com.abra.homework_9.observer.ManagerFactory
import com.abra.homework_9.repositories.DatabaseRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddCityFragment : DialogFragment() {
    private val fragmentScope = CoroutineScope(Dispatchers.Main + Job())
    private lateinit var repository: DatabaseRepository
    private val retrofit = RetrofitInitialization.getInstance()
    private var binding: FragmentAddCityBinding? = null
    private val manager = ManagerFactory.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddCityBinding.bind(inflater.inflate(R.layout.fragment_add_city, container, false))
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = DatabaseRepository(fragmentScope)
        setOkButtonListener()
        setCancelButtonListener()
    }

    private fun setOkButtonListener() {
        binding?.run {
            buttonOk.setOnClickListener {
                val cityName = etCityName.text.toString()
                if (cityName.isNotEmpty()) {
                    createRequest(cityName)
                } else {
                    Snackbar.make(it, "Write city or country name!", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun createRequest(cityName: String) {
        val whetherApi = retrofit.create(WeatherApi::class.java)
        val whetherApiCall = whetherApi.getWhetherForFiveDays(cityName)
        whetherApiCall.enqueue(object : Callback<WeatherRootObject> {
            override fun onResponse(call: Call<WeatherRootObject>, response: Response<WeatherRootObject>) {
                if (response.isSuccessful) {
                    fragmentScope.launch {
                        repository.addCity(CityData(cityName, response.body()?.city?.country ?: ""))
                        manager.setList(repository.getAllList())
                        dismiss()
                    }

                } else {
                    Snackbar.make(view as View, "No such city or country!", Snackbar.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<WeatherRootObject>, t: Throwable) {
                Snackbar.make(view as View, "Error occurred while loading data", Snackbar.LENGTH_SHORT).show()
            }

        })
    }

    private fun setCancelButtonListener() {
        binding?.buttonCancel?.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}