package com.abra.homework_6_1

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private var workList: ArrayList<WorkInfo> = arrayListOf()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WorkInfoAdapter
    companion object{
        private const val URI_PATH = "content://com.abra.homework_5.contentprovider.WorkInfoContentProvider/works_info"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recyclerViewWorks)
        setRecyclerSettings()
        val cursor = contentResolver.query(Uri.parse(URI_PATH),null,null,null,null)
        cursor?.run {
                moveToFirst()
                while (moveToNext()){
                    workList.add(WorkInfo(getString(getColumnIndex("date")),
                            getString(getColumnIndex("workName")),
                            getString(getColumnIndex("description")),
                            getString(getColumnIndex("cost")),
                            getString(getColumnIndex("status"))))

                }
                cursor.close()
        }

        adapter.updateLists(workList)
    }
    private fun setRecyclerSettings() {
        adapter = WorkInfoAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }
}