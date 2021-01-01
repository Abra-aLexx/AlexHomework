package com.abra.homework_5

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.SearchView
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
    private val ADD_WORK_ACTIVITY_CODE = 3
    private val EDIT_WORK_ACTIVITY_CODE = 4
    private lateinit var currentCarInfo: CarInfo
    private var lastUsedId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_list)
        toolbar = findViewById(R.id.toolBarWorkList)
        fab = findViewById(R.id.fabAddWork)
        imgButtonBack = findViewById(R.id.imgButtonBack2)
        recyclerView = findViewById(R.id.recyclerViewWorkList)
        database = DataBaseCarInfo.getDataBase(this)
        workInfoDAO = database.getWorkInfoDAO()
        if (intent!=null){
            currentCarInfo = intent.getParcelableExtra("carInfo")!!
        }
        adapter = if (workInfoDAO.getAllWorksForCar(currentCarInfo.id).isNotEmpty()){
            WorkInfoAdapter(this, workInfoDAO.getAllWorksForCar(currentCarInfo.id))
        }else {
            WorkInfoAdapter(this)
        }
        adapter.setOnWorkInfoItemClickListener(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        setSupportActionBar(toolbar)
        setButtonListeners()
        val sharedPreferences = getSharedPreferences("lastUsedId", MODE_PRIVATE)
        if (sharedPreferences.getInt("lastId", 0) != 0){
                lastUsedId = sharedPreferences.getInt("lastId", 0)
        }
    }

    private fun setButtonListeners(){
        imgButtonBack.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }
        fab.setOnClickListener {
            val intent = Intent(this, AddWorkActivity::class.java)
            intent.putExtra("currentCarInfo",currentCarInfo)
            startActivityForResult(intent,ADD_WORK_ACTIVITY_CODE)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.work_list_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.searchWorkList) {
            val searchView = item.actionView as SearchView
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            if (requestCode == ADD_WORK_ACTIVITY_CODE) {
                if (!data.getBooleanExtra("isButtonBack", false)) {
                    val workInfo = data.getParcelableExtra<WorkInfo>("workInfo")
                    if (workInfo != null) {
                        adapter.add(WorkInfo(++lastUsedId,workInfo,currentCarInfo.id))
                        workInfoDAO.addWorkInfo(WorkInfo(lastUsedId,workInfo,currentCarInfo.id))
                    }
                }
            }
            if (requestCode == EDIT_WORK_ACTIVITY_CODE) {
                if (!data.getBooleanExtra("isButtonBack", false)) {
                    val position = data.getIntExtra("position",-1)
                    val workInfo = data.getParcelableExtra<WorkInfo>("workInfo")
                    if (!data.getBooleanExtra("isRemoved", false)) {
                        if (workInfo != null) {
                            adapter.edit(WorkInfo(position+1, workInfo, currentCarInfo.id),position)
                            workInfoDAO.update(workInfo)
                        }
                    }else{
                        adapter.remove(position)
                        if (workInfo != null) {
                            workInfoDAO.delete(workInfo)
                        }
                    }
                }
            }
        }
    }

    override fun onWorkInfoItemClick(workInfo: WorkInfo ,position: Int) {
        val intent = Intent(this, EditWorkActivity::class.java)
        intent.putExtra("workInfo",workInfo)
        intent.putExtra("position",position)
        intent.putExtra("currentCarInfo",currentCarInfo)
        startActivityForResult(intent,EDIT_WORK_ACTIVITY_CODE)
    }
    override fun onDestroy() {
        val preference = getSharedPreferences("lastUsedId", MODE_PRIVATE)
        preference.edit().putInt("lastId", lastUsedId).apply()
        super.onDestroy()
    }
}