package com.abra.homework_9.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.abra.homework_9.R
import com.abra.homework_9.adapters.CityDataAdapter
import com.abra.homework_9.database.CityData
import com.abra.homework_9.databinding.FragmentChooseCityBinding
import com.abra.homework_9.observer.ManagerFactory
import com.abra.homework_9.observer.Observer
import com.abra.homework_9.repositories.DatabaseRepository
import com.abra.homework_9.repositories.SharedPrefRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ChooseCityFragment : Fragment(), Observer {
    private lateinit var loader: FragmentLoader
    private lateinit var preferencesRepository: SharedPrefRepository
    private val fragmentScope = CoroutineScope(Dispatchers.Main + Job())
    private lateinit var repository: DatabaseRepository
    private var binding: FragmentChooseCityBinding? = null
    private lateinit var adapter: CityDataAdapter
    private var checkedCityId = 0
    private val manager = ManagerFactory.getInstance()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentChooseCityBinding.bind(inflater.inflate(R.layout.fragment_choose_city, container, false))
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loader = requireActivity() as FragmentLoader
        preferencesRepository = SharedPrefRepository(context)
        repository = DatabaseRepository(fragmentScope)
        manager.subscribe(this)
        loadLastCityForecast(requireArguments())
        setAdapterSettings()
        loadCitiesList()
        setButtonsListeners()

    }

    private fun loadLastCityForecast(bundle: Bundle) {
        // этот флаг я ввел, чтобы при возврате на фрагмент с выбором городов не загружался сразу прогноз
        val isBackFromForecast = bundle.getBoolean("isBackFromForecast", false)
        if (!isBackFromForecast) {
            checkedCityId = preferencesRepository.getId()
            if (checkedCityId != 0) {
                loader.loadFragment(FragmentWeatherForecast::class.java,
                        bundleOf("id" to checkedCityId))
            }
        }
    }

    private fun setButtonsListeners() {
        binding?.run {
            buttonBack.setOnClickListener {
                checkedCityId = preferencesRepository.getId()
                if (checkedCityId != 0) {
                    loader.loadFragment(FragmentWeatherForecast::class.java,
                            bundleOf("id" to checkedCityId))
                } else {
                    Snackbar.make(view as View, "Choose city!", Snackbar.LENGTH_SHORT).show()
                }
            }
            buttonAdd.setOnClickListener {
                loader.loadDialogFragment()
            }
        }
    }

    private fun setAdapterSettings() {
        binding?.run {
            adapter = CityDataAdapter()
            recyclerViewCities.adapter = adapter
            recyclerViewCities.layoutManager = LinearLayoutManager(context)
        }
    }

    private fun loadCitiesList() {
        fragmentScope.launch {
            adapter.updateList(repository.getAllList())
        }
    }

    override fun update(list: List<CityData>) {
        adapter.updateList(list)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        manager.unsubscribe(this)
    }
}