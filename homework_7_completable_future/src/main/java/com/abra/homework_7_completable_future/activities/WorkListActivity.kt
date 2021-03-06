package com.abra.homework_7_completable_future.activities

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ImageButton
import androidx.appcompat.widget.SearchView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abra.homework_7_completable_future.R
import com.abra.homework_7_completable_future.adapters.WorkInfoAdapter
import com.abra.homework_7_completable_future.data.CarInfo
import com.abra.homework_7_completable_future.repositories.DatabaseRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

private const val ADD_WORK_ACTIVITY_CODE = 3
private const val EDIT_WORK_ACTIVITY_CODE = 4
private const val RESULT_CODE_BUTTON_BACK = 6

class WorkListActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var fab: FloatingActionButton
    private lateinit var imgButtonBack: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WorkInfoAdapter
    private lateinit var textCarName: TextView
    private lateinit var textCarModel: TextView
    private lateinit var noWorksAddedText: TextView
    private var currentCarId: Long = 0
    private lateinit var currentCar: CarInfo
    private lateinit var searchView: SearchView
    private lateinit var repository: DatabaseRepository

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
        setSupportActionBar(toolbar)
        repository = DatabaseRepository()
        getIntentData()
        setRecyclerSettings()
        setAdapterListener()
        setButtonListeners()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun setRecyclerSettings() {
        adapter = WorkInfoAdapter()
        setAdapterListener()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        repository.getAllWorkListForCar(currentCarId)
                .thenAcceptAsync({list ->
                    adapter.updateLists(list)
                    setNoWorksTextViewVisibility()
                }, mainExecutor)
//        repository.getAllWorkListForCar(currentCarId).thenApply { list ->
//            adapter.updateLists(list)
//            setNoWorksTextViewVisibility()
//        }
    }

    private fun setAdapterListener() {
        adapter.onWorkInfoItemClickListener = {
            val intent = Intent(this, EditWorkActivity::class.java)
            intent.putExtra("workInfo", it)
            startActivityForResult(intent, EDIT_WORK_ACTIVITY_CODE)
        }
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

    private fun getIntentData() {
        if (intent != null) {
            currentCar = intent.getParcelableExtra("carInfo") ?: CarInfo("", "", "", "")
            currentCarId = currentCar.id
            textCarName.text = currentCar.producer
            textCarModel.text = currentCar.model
        }
    }

    private fun setNoWorksTextViewVisibility() {
        if (adapter.itemCount != 0) noWorksAddedText.visibility = View.INVISIBLE
        else noWorksAddedText.visibility = View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.work_list_menu, menu)
        searchView = menu?.findItem(R.id.searchWorkList)?.actionView as SearchView
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itemInProgress -> {
                adapter.showByOrder(this, resources.getString(R.string.in_progress_lowe_case))
            }
            R.id.itemInPending -> {
                adapter.showByOrder(this, resources.getString(R.string.pending))
            }
            R.id.itemCompleted -> {
                adapter.showByOrder(this, resources.getString(R.string.completed_in_lower_case))
            }
            R.id.itemAll -> {
                adapter.showByOrder(this, "all")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_CODE_BUTTON_BACK) {
            /*
            * Была проблема с отображение TextView(No works added),
            * но потом понял, что надо менять видимость в главном потоке,
            * так как по правилам UI обновляется только в нем.
            * Честно не понял, почему mainExecutor поддерживается только
            * с версияй API >= 28
            * */
            repository.getAllWorkListForCar(currentCarId)
                    .thenAcceptAsync({list ->
                adapter.updateLists(list)
                setNoWorksTextViewVisibility()
            }, mainExecutor)
            if (!searchView.isIconified) {
                searchView.onActionViewCollapsed()
            }
        }
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_OK)
        finish()
        super.onBackPressed()
    }
}