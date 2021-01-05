package com.abra.homework_5

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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.io.FileOutputStream

class AddCarActivity : AppCompatActivity() {
    private val REQUEST_CODE_PHOTO = 1
    private val RESULT_CODE_BUTTON_BACK = 5
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
    private lateinit var dataBase: DataBaseCarInfo
    private lateinit var carInfoDAO: CarInfoDAO

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
        dataBase = DataBaseCarInfo.getDataBase(applicationContext)
        carInfoDAO = dataBase.getCarInfoDAO()
        createDirectory()
        setSupportActionBar(toolbar)
        setListeners()
    }

    fun setListeners() {
        imgButtonBack.setOnClickListener {
            showActivity(true)
        }
        imgButtonApply.setOnClickListener {
            showActivity(false)
        }
        fab.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, REQUEST_CODE_PHOTO)
        }
    }

    private fun showActivity(isButtonBack: Boolean) {
        val intent = Intent()
        if (!isButtonBack) {
            if (!photoWasLoaded) {
                pathToPicture = ""
            }
            val name = textName.text.toString()
            val producer = textProducer.text.toString()
            val model = textModel.text.toString()
            if (name.isNotEmpty() && producer.isNotEmpty() && model.isNotEmpty()) {
                val carInfo = CarInfo(pathToPicture, name, producer, model)
                carInfoDAO.add(carInfo)
                setResult(Activity.RESULT_OK,intent)
                finish()
            } else {
                Toast.makeText(this, "Fields can't be empty", Toast.LENGTH_SHORT).show()
            }
        } else {
            setResult(RESULT_CODE_BUTTON_BACK,intent)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data!=null) {
            if(data.extras?.get("data") !=null) {
                val photo = data.extras!!.get("data") as Bitmap?
                val path = "photo_${System.currentTimeMillis()}.jpg"
                if (photo != null) {
                    pathToPicture = "${carPictureDirectory.path}/${path}"
                    if (!carPictureDirectory.exists()) {
                        createDirectory()
                    }
                    val file = File(carPictureDirectory, path)
                    file.createNewFile()
                    val stream = FileOutputStream(file)
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                    carPhoto.setImageBitmap(photo)
                    noCarPhoto.visibility = View.INVISIBLE
                    photoWasLoaded = true
                    stream.flush()
                    stream.close()
                }
            }
        }
    }

    private fun createDirectory() {
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            carPictureDirectory = File("${getExternalFilesDir(Environment.DIRECTORY_PICTURES)}/CarPictures")
            if (!carPictureDirectory.exists()) {
                carPictureDirectory.mkdir()
            }
        }
    }
}