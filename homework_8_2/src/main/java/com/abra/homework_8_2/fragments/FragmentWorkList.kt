package com.abra.homework_8_2.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abra.homework_8_2.R
import com.abra.homework_8_2.adapters.WorkInfoAdapter
import com.abra.homework_8_2.data.CarInfo
import com.abra.homework_8_2.database.DataBaseCarInfo
import com.abra.homework_8_2.database.WorkInfoDAO
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FragmentWorkList : Fragment(R.layout.fragment_work_list) {
    private lateinit var toolbar: Toolbar
    private lateinit var fab: FloatingActionButton
    private lateinit var imgButtonBack: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WorkInfoAdapter
    private lateinit var database: DataBaseCarInfo
    private lateinit var workInfoDAO: WorkInfoDAO
    private lateinit var textCarName: TextView
    private lateinit var textCarModel: TextView
    private lateinit var noWorksAddedText: TextView
    private var currentCarId: Long = 0
    private lateinit var currentCar: CarInfo
    private lateinit var searchView: SearchView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(view) {
            toolbar = findViewById(R.id.toolBarWorkList)
            fab = findViewById(R.id.fabAddWork)
            imgButtonBack = findViewById(R.id.imgButtonBack2)
            recyclerView = findViewById(R.id.recyclerViewWorkList)
            textCarName = findViewById(R.id.tvWorkListCarName)
            textCarModel = findViewById(R.id.tvWorkListCarModel)
            noWorksAddedText = findViewById(R.id.tvNoWorksAdded)
        }
        setToolbarSettings()
        initDatabase()
        getData(requireArguments())
        setRecyclerSettings()
        setAdapterListener()
        setButtonListeners()
        setNoWorksTextViewVisibility()
    }

    private fun initDatabase() {
        database = DataBaseCarInfo.getDataBase(context as Context)
        workInfoDAO = database.getWorkInfoDAO()
    }

    private fun setRecyclerSettings() {
        adapter = WorkInfoAdapter(workInfoDAO.getAllWorksForCar(currentCarId))
        setAdapterListener()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun setAdapterListener() {
        adapter.onWorkInfoItemClickListener = {
            requireActivity().supportFragmentManager.commit {
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                replace(R.id.fragmentContainer, FragmentEditWorkInfo::class.java,
                        bundleOf("workInfo" to it, "carInfo" to currentCar))
            }
        }
    }

    private fun setButtonListeners() {
        imgButtonBack.setOnClickListener {
            requireActivity().supportFragmentManager.commit {
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                replace<FragmentCarList>(R.id.fragmentContainer)
            }
        }
        fab.setOnClickListener {
            requireActivity().supportFragmentManager.commit {
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                replace(R.id.fragmentContainer, FragmentAddWork::class.java,
                        bundleOf("currentCarId" to currentCarId, "carInfo" to currentCar))
            }
        }
    }

    private fun getData(bundle: Bundle) {
        currentCar = bundle.getParcelable("carInfo") ?: CarInfo()
        with(currentCar) {
            currentCarId = id
            textCarName.text = producer
            textCarModel.text = model
        }
    }

    private fun setNoWorksTextViewVisibility() {
        if (adapter.itemCount != 0) noWorksAddedText.visibility = View.INVISIBLE
        else noWorksAddedText.visibility = View.VISIBLE
    }

    private fun setToolbarSettings() {
        searchView = toolbar.menu.findItem(R.id.searchWorkList)?.actionView as SearchView
        searchView.apply {
            imeOptions = EditorInfo.IME_ACTION_DONE
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?) = false

                override fun onQueryTextChange(p0: String?): Boolean {
                    adapter.filter.filter(p0)
                    return false
                }
            })
        }
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.itemInProgress -> {
                    adapter.showByOrder(context as Context, resources.getString(R.string.in_progress_lowe_case))
                }
                R.id.itemInPending -> {
                    adapter.showByOrder(context as Context, resources.getString(R.string.pending))
                }
                R.id.itemCompleted -> {
                    adapter.showByOrder(context as Context, resources.getString(R.string.completed_in_lower_case))
                }
                R.id.itemAll -> {
                    adapter.showByOrder(context as Context, "all")
                }
            }
            true
        }
    }
}