package com.abra.homework_5

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.io.FileOutputStream

class EditCarInfoActivity : AppCompatActivity() {
    private val REQUEST_CODE_PHOTO = 1
    private lateinit var textName: EditText
    private lateinit var textProducer: EditText
    private lateinit var textModel: EditText
    private lateinit var imgButtonBack: ImageButton
    private lateinit var imgButtonApply: ImageButton
    private lateinit var fab: FloatingActionButton
    private lateinit var carPhoto: ImageView
    private lateinit var toolbar: Toolbar
    private var photoWasLoaded: Boolean = false
    private lateinit var pathToPicture: String
    private var position: Int = -1
    private lateinit var carPictureDirectory: File
    private lateinit var currentCarInfo: CarInfo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_car_info)
        toolbar = findViewById(R.id.toolBarEdit)
        carPhoto = findViewById(R.id.imageCarPhoto1)
        textName = findViewById(R.id.etOwnerName1)
        textProducer = findViewById(R.id.etProducer1)
        textModel = findViewById(R.id.etModel1)
        imgButtonBack = findViewById(R.id.imgButtonBack1)
        imgButtonApply = findViewById(R.id.imgButtonApply1)
        fab = findViewById(R.id.fabLoadPhoto1)
        createDirectory()
        setSupportActionBar(toolbar)
        setListeners()
        loadDataFromIntent()
    }
    private fun loadDataFromIntent(){
        val carInfo = intent.getParcelableExtra<CarInfo>("carInfo")
        if (carInfo != null) {
            currentCarInfo = carInfo
        }
        position = intent.getIntExtra("position", -1)
        if (carInfo != null) {
            val path = carInfo.pathToPicture
            val file = File(path)
            if (file.exists()) {
                if (path == "") {
                    carPhoto.setImageResource(R.drawable.default_icon)
                } else {
                    Glide.with(this).load(path).into(carPhoto)
                    photoWasLoaded = true
                    if (path != null) {
                        pathToPicture = path
                    }
                }
            }
            textName.setText(carInfo.name)
            textProducer.setText(carInfo.producer)
            textModel.setText(carInfo.model)
        }
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
            val name = textName.text.toString()
            val producer = textName.text.toString()
            val model = textModel.text.toString()
            if (name.isNotEmpty() && producer.isNotEmpty() && model.isNotEmpty()) {
                if (!photoWasLoaded) {
                    pathToPicture = ""
                }
                // странно, что работает только при создании нового ключа Long, а с carInfo.id не хочет
                intent.putExtra("carInfo", CarInfo(currentCarInfo.id, pathToPicture, name, producer, model))
                intent.putExtra("isButtonBack", isButtonBack)
                intent.putExtra("position", position)
                setResult(Activity.RESULT_OK,intent)
                finish()
            } else {
                Toast.makeText(this, "Fields can't be empty", Toast.LENGTH_SHORT).show()
            }
        } else {
            intent.putExtra("isButtonBack", isButtonBack)
            setResult(Activity.RESULT_OK,intent)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val photo = data!!.extras!!.get("data") as Bitmap?
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
            photoWasLoaded = true
            stream.flush()
            stream.close()
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