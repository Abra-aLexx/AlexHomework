package com.abra.homework_6_2

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private val photoList = arrayListOf<PhotoInfo>()
    private lateinit var recyclerPhotos: RecyclerView
    private lateinit var adapter: PhotoInfoAdapter
    private val MY_PERMISSION_CODE = 101
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerPhotos = findViewById(R.id.recyclerPhotos)
        adapter = PhotoInfoAdapter()
        setRecyclerSettings()
        loadAllPhotos()
        if(ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),MY_PERMISSION_CODE)
        }else{
            loadAllPhotos()
        }
    }
    private fun setRecyclerSettings(){
        recyclerPhotos.adapter = adapter
        recyclerPhotos.layoutManager = GridLayoutManager(this,3)
    }
    private fun loadAllPhotos(){
        val uri: Uri
        var cursor: Cursor?
        val column_index_data: Int
        var absolutePathOfImage: String
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        val orderBy = "${MediaStore.Video.Media.DATE_TAKEN} DESC"
        cursor = contentResolver.query(uri,projection,null,null,orderBy)
        if (cursor != null) {
            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
            while (cursor.moveToNext()){
                absolutePathOfImage = cursor.getString(column_index_data)
                photoList.add(PhotoInfo(absolutePathOfImage))
            }
            adapter.updateList(photoList)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_PERMISSION_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Read external storage permission granted",Toast.LENGTH_SHORT).show()
                loadAllPhotos()
            }
        }else{
            Toast.makeText(this, "Read external storage permission denied",Toast.LENGTH_SHORT).show()
        }
    }
}