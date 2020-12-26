package com.abra.homework_5

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AddCarActivity : AppCompatActivity() {
    private val REQUEST_CODE_PHOTO = 1
    private lateinit var textName: EditText
    private lateinit var textProducer: EditText
    private lateinit var textModel: EditText
    private lateinit var imgButtonBack: ImageButton
    private lateinit var imgButtonApply: ImageButton
    private lateinit var fab: FloatingActionButton
    private lateinit var carPhoto: ImageView
    private lateinit var noCarPhoto: TextView
    private var bitmap: Bitmap? = null
    private lateinit var toolbar: Toolbar
    private var photoWasLoaded: Boolean = false
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
                bitmap = null
            }
            val name = textName.text.toString()
            val producer = textName.text.toString()
            val model = textModel.text.toString()
            if (name.isNotEmpty() && producer.isNotEmpty() && model.isNotEmpty()) {
                intent.putExtra("carInfo", CarInfo(bitmap, textName.text.toString(), textProducer.text.toString(), textModel.text.toString()))
                intent.putExtra("isButtonBack", isButtonBack)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }else{
                Toast.makeText(this,"Fields can't be empty",Toast.LENGTH_SHORT).show()
            }
        }
        if(isButtonBack){
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
        noCarPhoto.visibility = View.INVISIBLE
        photoWasLoaded = true
    }
}