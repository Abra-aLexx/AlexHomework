package com.abra.homework_9.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.abra.homework_9.R
import com.abra.homework_9.databinding.FragmentCurrentRegionBinding
import com.abra.homework_9.network.*
import com.abra.homework_9.repositories.DatabaseRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentWeatherForecast : Fragment(R.layout.fragment_current_region) {
    private lateinit var fragmentLoader: FragmentLoader
    private val fragmentScope = CoroutineScope(Dispatchers.Main + Job())
    private val retrofit = RetrofitInitialization.getInstance()
    private lateinit var repository: DatabaseRepository
    private var binding: FragmentCurrentRegionBinding? = null
    private lateinit var loader: DataLoader
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCurrentRegionBinding.bind(inflater.inflate(R.layout.fragment_current_region, container, false))
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.visibility = View.INVISIBLE
        fragmentLoader = requireActivity() as FragmentLoader
        repository = DatabaseRepository(fragmentScope)
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
            createRequest(repository.getCityById(id).name)
        }
    }

    private fun createRequest(cityName: String) {
        val whetherApi = retrofit.create(WeatherApi::class.java)
        val whetherApiCall = whetherApi.getWhetherForFiveDays(cityName)
        whetherApiCall.enqueue(object : Callback<WeatherRootObject> {
            override fun onResponse(call: Call<WeatherRootObject>, response: Response<WeatherRootObject>) {
                if (response.isSuccessful) {
                    response.body()?.run {
                        loader = DataLoader(this)
                        loader.setWhetherForecast(binding, context as Context)
                        view?.visibility = View.VISIBLE
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

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}