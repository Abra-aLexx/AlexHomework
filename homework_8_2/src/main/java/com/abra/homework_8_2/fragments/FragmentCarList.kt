package com.abra.homework_8_2.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abra.homework_8_2.R
import com.abra.homework_8_2.adapters.CarInfoAdapter
import com.abra.homework_8_2.database.CarInfoDAO
import com.abra.homework_8_2.database.DataBaseCarInfo
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FragmentCarList : Fragment(R.layout.fragment_car_list) {
    private lateinit var loader: FragmentLoader
    private lateinit var toolbar: Toolbar
    private lateinit var fab: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CarInfoAdapter
    private lateinit var noCarsAddedText: TextView
    private lateinit var database: DataBaseCarInfo
    private lateinit var carInfoDAO: CarInfoDAO
    private lateinit var searchView: SearchView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(view) {
            toolbar = findViewById(R.id.toolBarCarList)
            fab = findViewById(R.id.fabAddCar)
            recyclerView = findViewById(R.id.recyclerViewCars)
            noCarsAddedText = findViewById(R.id.tvNoCarsAdded)
        }
        loader = requireActivity() as FragmentLoader
        setSearchViewSettings()
        initDatabase()
        setRecyclerSettings()
        setFabListener()
        setAdapterListeners()
        setNoCarsTextViewVisibility()
    }

    private fun initDatabase() {
        database = DataBaseCarInfo.getDataBase(context as Context)
        carInfoDAO = database.getCarInfoDAO()
    }

    private fun setFabListener() {
        fab.setOnClickListener {
            loader.loadFragment(FragmentAddCar(),
                    FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        }
    }

    private fun setRecyclerSettings() {
        adapter = CarInfoAdapter(carInfoDAO.getAll().sortedBy { it.producer.toLowerCase() })
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun setNoCarsTextViewVisibility() {
        if (adapter.itemCount != 0) noCarsAddedText.visibility = View.INVISIBLE
        else noCarsAddedText.visibility = View.VISIBLE
    }

    private fun setAdapterListeners() {
        adapter.onEditIconClickListener = {
            loader.loadFragment(FragmentEditCarInfo::class.java,
                    FragmentTransaction.TRANSIT_FRAGMENT_OPEN,
                    bundleOf("carInfo" to it))
        }
        adapter.onCarInfoShowWorkListClickListener = {
            loader.loadFragment(FragmentWorkList::class.java,
                    FragmentTransaction.TRANSIT_FRAGMENT_OPEN,
                    bundleOf("carInfo" to it))
        }
    }

    private fun setSearchViewSettings() {
        searchView = toolbar.menu.findItem(R.id.search)?.actionView as SearchView
        searchView.apply {
            imeOptions = EditorInfo.IME_ACTION_DONE
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(text: String?) = false

                override fun onQueryTextChange(text: String?): Boolean {
                    adapter.filter.filter(text)
                    return false
                }
            })
        }
    }
}