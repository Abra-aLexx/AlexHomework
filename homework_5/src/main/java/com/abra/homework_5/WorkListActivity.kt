package com.abra.homework_5

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.widget.SearchView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class WorkListActivity : AppCompatActivity(), WorkInfoAdapter.OnWorkInfoItemClickListener {
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
    private val ADD_WORK_ACTIVITY_CODE = 3
    private val EDIT_WORK_ACTIVITY_CODE = 4
    private val RESULT_CODE_BUTTON_BACK = 6
    private var currentCarId: Long = 0
    private lateinit var currentCar: CarInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_list)
        toolbar = findViewById(R.id.toolBarWorkList)
        fab = findViewById(R.id.fabAddWork)
        imgButtonBack = findViewById(R.id.imgButtonBack2)
        recyclerView = findViewById(R.id.recyclerViewWorkList)
        textCarName = findViewById(R.id.tvWorkListCarName)
        textCarModel = findViewById(R.id.tvWorkListCarModel)
        noWorksAddedText = findViewById(R.id.tvNoWorksAdded)
        database = DataBaseCarInfo.getDataBase(applicationContext)
        workInfoDAO = database.getWorkInfoDAO()
        if (intent != null) {
            currentCar = intent.getParcelableExtra("carInfo")!!
            currentCarId = currentCar.id!!
            textCarName.text = currentCar.name
            textCarModel.text = currentCar.model
        }
        adapter = if (workInfoDAO.getAllWorksForCar(currentCarId).isNotEmpty()) {
            WorkInfoAdapter(this, workInfoDAO.getAllWorksForCar(currentCarId))
        } else {
            WorkInfoAdapter(this)
        }
        adapter.setOnWorkInfoItemClickListener(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        setSupportActionBar(toolbar)
        setButtonListeners()
        if (adapter.itemCount != 0) noWorksAddedText.visibility = View.INVISIBLE
    }

    private fun setButtonListeners() {
        imgButtonBack.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }
        fab.setOnClickListener {
            val intent = Intent(this, AddWorkActivity::class.java)
            intent.putExtra("currentCarId", currentCarId)
            startActivityForResult(intent, ADD_WORK_ACTIVITY_CODE)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.work_list_menu, menu)
        val searchView = menu?.findItem(R.id.searchWorkList)?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?) = false

            override fun onQueryTextChange(p0: String?): Boolean {
                adapter.filter.filter(p0)
                return false
            }
        })
        return true
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return super.onOptionsItemSelected(item)
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
            if (resultCode != RESULT_CODE_BUTTON_BACK) {
                adapter.updateLists(workInfoDAO.getAllWorksForCar(currentCarId))
                if (adapter.itemCount != 0) noWorksAddedText.visibility = View.INVISIBLE
            }
    }

    override fun onWorkInfoItemClick(workInfo: WorkInfo, position: Int) {
        val intent = Intent(this, EditWorkActivity::class.java)
        intent.putExtra("workInfo", workInfo)
        startActivityForResult(intent, EDIT_WORK_ACTIVITY_CODE)
    }
}