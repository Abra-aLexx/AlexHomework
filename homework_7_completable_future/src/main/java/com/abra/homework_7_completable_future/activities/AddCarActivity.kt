package com.abra.homework_7_completable_future.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.abra.homework_7_completable_future.data.CarInfo
import com.abra.homework_7_completable_future.R
import com.abra.homework_7_completable_future.functions.saveImage
import com.abra.homework_7_completable_future.repositories.DatabaseRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File

private const val REQUEST_CODE_PHOTO = 1
private const val RESULT_CODE_BUTTON_BACK = 5

class AddCarActivity : AppCompatActivity() {
    private lateinit var textName: EditText
    private lateinit var textProducer: EditText
    private lateinit var textModel: EditText
    private lateinit var imgButtonBack: ImageButton
    private lateinit var imgButtonApply: ImageButton
    private lateinit var fab: FloatingActionButton
    private lateinit var carPhoto: ImageView
    private lateinit var noCarPhoto: TextView
    private lateinit var toolbar: Toolbar
    private var photoWasLoaded: Boolean = false
    private lateinit var carPictureDirectory: File
    private lateinit var pathToPicture: String
    private lateinit var repository: DatabaseRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_car)
        toolbar = findViewById(R.id.toolBarAdd)
        carPhoto = findViewById(R.id.imageCarPhoto)
        textName = findViewById(R.id.etOwnerName)
        textProducer = findViewById(R.id.etProducer)
        textModel = findViewById(R.id.etModel)
        imgButtonBack = findViewById(R.id.imgButtonBack)
        imgButtonApply = findViewById(R.id.imgButtonApply)
        fab = findViewById(R.id.fabLoadPhoto)
        noCarPhoto = findViewById(R.id.tvNoPhoto)
        repository = DatabaseRepository()
        createDirectoryForPictures()
        setSupportActionBar(toolbar)
        setListeners()
    }

    private fun setListeners() {
        imgButtonBack.setOnClickListener {
            backToPreviousActivity()
        }
        imgButtonApply.setOnClickListener {
            addCarInfoAndBackToPreviousActivity()
        }
        fab.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, REQUEST_CODE_PHOTO)
        }
    }

    private fun addCarInfoAndBackToPreviousActivity() {
        val intent = Intent()
        if (!photoWasLoaded) {
            pathToPicture = ""
        }
        val name = textName.text.toString()
        val producer = textProducer.text.toString()
        val model = textModel.text.toString()
        if (name.isNotEmpty() && producer.isNotEmpty() && model.isNotEmpty()) {
            val carInfo = CarInfo(pathToPicture, name, producer, model)
            repository.addCar(carInfo)
            setResult(Activity.RESULT_OK, intent)
            finish()
        } else {
            Toast.makeText(this, "Fields can't be empty", Toast.LENGTH_SHORT).show()
        }
    }

    private fun backToPreviousActivity() {
        setResult(RESULT_CODE_BUTTON_BACK, intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            if (data.extras != null) {
                if (data.extras?.get("data") != null) {
                    val photo = data.extras?.get("data") as Bitmap?
                    if (photo != null) {
                        pathToPicture = saveImage(photo, carPhoto, carPictureDirectory)
                        photoWasLoaded = true
                        noCarPhoto.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }

    private fun createDirectoryForPictures() {
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            carPictureDirectory = File("${getExternalFilesDir(Environment.DIRECTORY_PICTURES)}/CarPictures")
            if (!carPictureDirectory.exists()) {
                carPictureDirectory.mkdir()
            }
        }
    }
}