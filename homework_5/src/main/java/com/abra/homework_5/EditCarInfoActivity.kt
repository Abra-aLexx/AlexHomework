package com.abra.homework_5

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.FieldPosition

class EditCarInfoActivity : AppCompatActivity() {
    private val REQUEST_CODE_PHOTO = 1
    private lateinit var textName: EditText
    private lateinit var textProducer: EditText
    private lateinit var textModel: EditText
    private lateinit var imgButtonBack: ImageButton
    private lateinit var imgButtonApply: ImageButton
    private lateinit var fab: FloatingActionButton
    private lateinit var carPhoto: ImageView
    private var bitmap: Bitmap? = null
    private lateinit var toolbar: Toolbar
    private var photoWasLoaded: Boolean = false
    private var position: Int = -1
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
        setSupportActionBar(toolbar)
        setListeners()
        val intent = intent
        if (intent != null) {
            val carInfo = intent.getParcelableExtra<CarInfo>("carInfo")
            position = intent.getIntExtra("position", -1)
            if (carInfo != null) {
                val loadedPhoto = carInfo.carBitmap
                if (loadedPhoto == null) {
                    carPhoto.setImageResource(R.drawable.default_icon)
                } else {
                    carPhoto.setImageBitmap(loadedPhoto)
                    photoWasLoaded = true
                    bitmap = loadedPhoto
                }
                textName.setText(carInfo.name)
                textProducer.setText(carInfo.producer)
                textModel.setText(carInfo.model)
            }
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
                    bitmap = null
                }
                intent.putExtra("carInfo", CarInfo(bitmap, textName.text.toString(), textProducer.text.toString(), textModel.text.toString()))
                intent.putExtra("isButtonBack", isButtonBack)
                intent.putExtra("position", position)
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(this, "Fields can't be empty", Toast.LENGTH_SHORT).show()
            }
        }
        if (isButtonBack) {
            intent.putExtra("isButtonBack", isButtonBack)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val photo = data!!.extras!!.get("data") as Bitmap?
        if (photo != null) {
            bitmap = photo
        }
        carPhoto.setImageBitmap(photo)
        photoWasLoaded = true
    }
}