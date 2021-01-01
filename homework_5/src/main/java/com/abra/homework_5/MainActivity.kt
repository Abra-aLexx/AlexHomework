package com.abra.homework_5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), CarInfoAdapter.OnEditIconClickListener, CarInfoAdapter.OnCarInfoClickListener {
    private lateinit var fab: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CarInfoAdapter
    private lateinit var noCarsAddedText: TextView
    private lateinit var database: DataBaseCarInfo
    private lateinit var carInfoDAO: CarInfoDAO
    private val ADD_ACTIVITY_CODE = 1
    private val EDDIT_ACTIVITY_CODE = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fab = findViewById(R.id.fabAddCar)
        recyclerView = findViewById(R.id.recyclerViewCars)
        noCarsAddedText = findViewById(R.id.tvNoCarsAdded)
        database = DataBaseCarInfo.getDataBase(this)
        carInfoDAO = database.getCarInfoDAO()
        adapter = if (carInfoDAO.getAll().isNotEmpty()) {
            CarInfoAdapter(this, carInfoDAO.getAll())
        } else {
            CarInfoAdapter(this)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        setFabListener()
        adapter.setOnEditIconClickListener(this)
        adapter.setOnCarInfoClickListener(this)
        if (adapter.itemCount != 0) noCarsAddedText.visibility = View.INVISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.search) {
            val searchView = item.actionView as SearchView
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setFabListener() {
        fab.setOnClickListener {
            val intent = Intent(this, AddCarActivity::class.java)
            startActivityForResult(intent, ADD_ACTIVITY_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            if (requestCode == ADD_ACTIVITY_CODE) {
                if (!data.getBooleanExtra("isButtonBack", false)) {
                    val carInfo = data.getParcelableExtra<CarInfo>("carInfo")
                    if (carInfo != null) {
                        if(adapter.itemCount==0) {
                            adapter.add(CarInfo(adapter.itemCount+1, carInfo))
                        }else{
                            adapter.add(CarInfo(adapter.itemCount, carInfo))
                        }
                        carInfoDAO.add(CarInfo(adapter.itemCount, carInfo))
                    }
                }
            }
            if (requestCode == EDDIT_ACTIVITY_CODE) {
                if (!data.getBooleanExtra("isButtonBack", false)) {
                    val carInfo = data.getParcelableExtra<CarInfo>("carInfo")
                    val position = data.getIntExtra("position", -1)
                    if (carInfo != null && position != -1) {
                        carInfo.id = position + 1
                        adapter.edit(CarInfo(carInfo.id, carInfo), position)
                        carInfoDAO.update(carInfo)
                    }
                }
            }
        }
        if (adapter.itemCount != 0) noCarsAddedText.visibility = View.INVISIBLE
    }

    override fun onEditIconClick(carInfo: CarInfo, position: Int) {
        val intent = Intent(this, EditCarInfoActivity::class.java)
        intent.putExtra("carInfo", carInfo)
        intent.putExtra("position", position)
        startActivityForResult(intent, EDDIT_ACTIVITY_CODE)
    }

    override fun onCarInfoClick(carInfo: CarInfo, position: Int) {
        val intent = Intent(this, WorkListActivity::class.java)
        intent.putExtra("carInfo", CarInfo(position+1,carInfo))
        startActivityForResult(intent, EDDIT_ACTIVITY_CODE)
    }

}