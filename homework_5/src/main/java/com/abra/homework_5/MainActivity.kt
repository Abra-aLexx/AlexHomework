package com.abra.homework_5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.SearchView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), CarInfoAdapter.OnEditIconClickListener, CarInfoAdapter.OnCarInfoClickListener {
    private lateinit var fab: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CarInfoAdapter
    private lateinit var noCarsAddedText: TextView
    private lateinit var database: DataBaseCarInfo
    private lateinit var carInfoDAO: CarInfoDAO
    private lateinit var searchView: SearchView
    private val ADD_ACTIVITY_CODE = 1
    private val EDDIT_ACTIVITY_CODE = 2
    private val WORK_LIST_ACTIVITY = 10
    private val RESULT_CODE_BUTTON_BACK = 5
    private var searchWasUsed: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fab = findViewById(R.id.fabAddCar)
        recyclerView = findViewById(R.id.recyclerViewCars)
        noCarsAddedText = findViewById(R.id.tvNoCarsAdded)
        database = DataBaseCarInfo.getDataBase(this)
        carInfoDAO = database.getCarInfoDAO()
        adapter = if (carInfoDAO.getAll().isNotEmpty()) {
            CarInfoAdapter(this, carInfoDAO.getAll()).also { it.sortByCarName(carInfoDAO.getAll()) }
        } else {
            CarInfoAdapter(this)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        setFabListener()
        adapter.setOnEditIconClickListener(this)
        adapter.setOnCarInfoClickListener(this)
        writeLogToFile()
        if (adapter.itemCount != 0) noCarsAddedText.visibility = View.INVISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        searchView = menu?.findItem(R.id.search)?.actionView as SearchView
        searchView.imeOptions = EditorInfo.IME_ACTION_DONE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?) = false

            override fun onQueryTextChange(p0: String?): Boolean {
                searchWasUsed = true
                adapter.filter.filter(p0)
                return false
            }
        })
        return true
    }

    private fun setFabListener() {
        fab.setOnClickListener {
            val intent = Intent(this, AddCarActivity::class.java)
            startActivityForResult(intent, ADD_ACTIVITY_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_CODE_BUTTON_BACK) {
            adapter.sortByCarName(carInfoDAO.getAll())
            adapter.sortByCarName(carInfoDAO.getAll())
            if (searchWasUsed) {
                searchView.onActionViewCollapsed()
                searchWasUsed = false
            }
        }
        if (adapter.itemCount != 0) noCarsAddedText.visibility = View.INVISIBLE
    }

    override fun onEditIconClick(carInfo: CarInfo) {
        val intent = Intent(this, EditCarInfoActivity::class.java)
        intent.putExtra("carInfo", carInfo)
        startActivityForResult(intent, EDDIT_ACTIVITY_CODE)
    }

    override fun onCarInfoClick(carInfo: CarInfo) {
        val intent = Intent(this, WorkListActivity::class.java)
        intent.putExtra("carInfo", carInfo)
        startActivityForResult(intent, WORK_LIST_ACTIVITY)
    }

    private fun writeLogToFile() {
        val logList = StringBuilder()
        val file = File(filesDir, "Logs.txt")
        if (!file.exists()) {
            file.createNewFile()
        }
        logList.append("${file.readText()} \n")
        val simpleDateFormat = SimpleDateFormat.getDateTimeInstance()
        val date = simpleDateFormat.format(Date())
        logList.append(date)
        file.writeText(logList.toString())
    }
}