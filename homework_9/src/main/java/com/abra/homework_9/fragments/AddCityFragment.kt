package com.abra.homework_9.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.abra.homework_9.R
import com.abra.homework_9.database.CityData
import com.abra.homework_9.databinding.FragmentAddCityBinding
import com.abra.homework_9.observer.ManagerFactory
import com.abra.homework_9.repositories.DatabaseRepository
import com.abra.homework_9.repositories.RequestRepository
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AddCityFragment : DialogFragment() {
    private val disposableContainer: CompositeDisposable = CompositeDisposable()
    private val fragmentScope = CoroutineScope(Dispatchers.Main + Job())
    private lateinit var repository: DatabaseRepository
    private lateinit var requestRepository: RequestRepository
    private var binding: FragmentAddCityBinding? = null
    private val manager = ManagerFactory.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddCityBinding.bind(inflater.inflate(R.layout.fragment_add_city, container, false))
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = DatabaseRepository(fragmentScope)
        requestRepository = RequestRepository()
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
        requestRepository.createRequest(cityName)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { data -> prepareLoadedData(cityName, data.city.country) },
                        { Snackbar.make(view as View, "No such city or country!", Snackbar.LENGTH_SHORT).show() }
                )
                .also { disposableContainer.add(it) }
    }

    private fun prepareLoadedData(cityName: String, country: String) {
        fragmentScope.launch {
            repository.addCity(CityData(cityName, country))
            manager.setList(repository.getAllList())
            dismiss()
        }
    }

    private fun setCancelButtonListener() {
        binding?.buttonCancel?.setOnClickListener {
            dismiss()
        }
    }

    override fun onStop() {
        super.onStop()
        disposableContainer.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}