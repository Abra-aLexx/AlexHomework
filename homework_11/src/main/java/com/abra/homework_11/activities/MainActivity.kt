package com.abra.homework_11.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.abra.homework_11.R
import com.abra.homework_11.adapter.LogDataAdapter
import com.abra.homework_11.databinding.ActivityMainBinding
import com.abra.homework_11.json_structure.LogData
import com.abra.homework_11.observer.LogsManager
import com.abra.homework_11.observer.Observer
import com.abra.homework_11.viewmodel.LogsViewModel
import com.abra.homework_11.viewmodel.LogsViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class MainActivity : AppCompatActivity(), Observer {
    private lateinit var viewModel: LogsViewModel
    private var binding: ActivityMainBinding? = null
    private lateinit var adapter: LogDataAdapter
    private val manager = LogsManager.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding?.root)
        viewModel = ViewModelProvider(this,
                LogsViewModelFactory(applicationContext,
                        CoroutineScope(Dispatchers.Main + Job())))
                .get(LogsViewModel::class.java)
        manager.subscribe(this)
        setRecyclerViewSettings()
        getData()
        setAdapterListener()
    }

    private fun setRecyclerViewSettings() {
        binding?.run {
            adapter = LogDataAdapter()
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun getData() {
        viewModel.logsLiveData.observe(this, {
            adapter.updateList(it.logsList)
        })
        viewModel.requestLogs()
    }

    private fun setAdapterListener() {
        adapter.onItemClickListener = {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, "Event name - ${it.actionName}\nHappened on ${it.date} in ${it.time}")
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, "Choose client"))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.sort_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itemDate -> adapter.sortByDate()
            R.id.itemTime -> adapter.sortByTime()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun update(list: MutableList<LogData>) {
        adapter.updateList(list)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        manager.unsubscribe(this)
    }
}