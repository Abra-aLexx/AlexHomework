package com.abra.homework_5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), CarInfoAdapter.OnEditIconClickListener {
    private lateinit var fab: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CarInfoAdapter
    private lateinit var noCarsAddedText: TextView

    private val ADD_ACTIVITY_CODE = 1
    private val EDDIT_ACTIVITY_CODE = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fab = findViewById(R.id.fabAddCar)
        recyclerView = findViewById(R.id.recyclerViewCars)
        noCarsAddedText = findViewById(R.id.tvNoCarsAdded)
        adapter = CarInfoAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        setFabListener()
        adapter.setOnEditIconClickListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.search){
            val searchView = item.actionView as SearchView
        }
        return super.onOptionsItemSelected(item)
    }
    private fun setFabListener(){
        fab.setOnClickListener {
            val intent = Intent(this, AddCarActivity::class.java)
            startActivityForResult(intent,ADD_ACTIVITY_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(data!=null) {
            if(requestCode == ADD_ACTIVITY_CODE) {
                if(!data.getBooleanExtra("isButtonBack",false)) {
                    val carInfo = data.getParcelableExtra<CarInfo>("carInfo")
                    if (carInfo != null) {
                        adapter.add(carInfo)
                    }
                }
            }
            if(requestCode == EDDIT_ACTIVITY_CODE) {
                if(!data.getBooleanExtra("isButtonBack",false)) {
                    val carInfo = data.getParcelableExtra<CarInfo>("carInfo")
                    val position = data.getIntExtra("position",-1)
                    if (carInfo != null && position!=-1) {
                        adapter.edit(carInfo,position)
                    }
                }
            }
        }
            if (adapter.getList().size != 0) findViewById<TextView>(R.id.tvNoCarsAdded).visibility = View.INVISIBLE
    }

    override fun onEditIconClick(carInfo: CarInfo, position: Int) {
        val intent = Intent(this, EditCarInfoActivity::class.java)
        intent.putExtra("carInfo",carInfo)
        intent.putExtra("position",position)
        startActivityForResult(intent,EDDIT_ACTIVITY_CODE)
    }
}