package com.abra.homework_7_rx_java.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.SearchView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abra.homework_7_rx_java.R
import com.abra.homework_7_rx_java.adapters.CarInfoAdapter
import com.abra.homework_7_rx_java.repositories.DatabaseRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

private const val ADD_ACTIVITY_CODE = 1
private const val EDIT_ACTIVITY_CODE = 2
private const val WORK_LIST_ACTIVITY = 10
private const val RESULT_CODE_BUTTON_BACK = 5

class MainActivity : AppCompatActivity() {
    private lateinit var fab: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CarInfoAdapter
    private lateinit var noCarsAddedText: TextView
    private lateinit var searchView: SearchView
    private lateinit var repository: DatabaseRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fab = findViewById(R.id.fabAddCar)
        recyclerView = findViewById(R.id.recyclerViewCars)
        noCarsAddedText = findViewById(R.id.tvNoCarsAdded)
        DatabaseRepository.initDatabase(applicationContext)
        repository = DatabaseRepository()
        setRecyclerSettings()
        setFabListener()
        setAdapterListeners()
        writeLogToFile()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        searchView = menu?.findItem(R.id.search)?.actionView as SearchView
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
        return true
    }

    private fun setFabListener() {
        fab.setOnClickListener {
            val intent = Intent(this, AddCarActivity::class.java)
            startActivityForResult(intent, ADD_ACTIVITY_CODE)
        }
    }

    private fun setRecyclerSettings() {
        adapter = CarInfoAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        repository.getAllList()
                .subscribe { list ->
                    adapter.updateList(list.sortedBy { it.producer.toLowerCase() })
                    setNoCarsTextViewVisibility()
                }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_CODE_BUTTON_BACK) {
            repository.getAllList()
                    .subscribe { list ->
                        adapter.updateList(list.sortedBy { it.producer.toLowerCase() })
                        setNoCarsTextViewVisibility()
                    }
            if (!searchView.isIconified) {
                searchView.onActionViewCollapsed()
            }
        }
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

    private fun setNoCarsTextViewVisibility() {
        if (adapter.itemCount != 0) noCarsAddedText.visibility = View.INVISIBLE
        else noCarsAddedText.visibility = View.VISIBLE
    }

    private fun setAdapterListeners() {
        adapter.onEditIconClickListener = {
            val intent = Intent(this, EditCarInfoActivity::class.java)
            intent.putExtra("carInfo", it)
            startActivityForResult(intent, EDIT_ACTIVITY_CODE)
        }
        adapter.onCarInfoShowWorkListClickListener = {
            val intent = Intent(this, WorkListActivity::class.java)
            intent.putExtra("carInfo", it)
            startActivityForResult(intent, WORK_LIST_ACTIVITY)
        }
    }
}