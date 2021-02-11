package com.abra.homework_8_2.fragments

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.abra.homework_8_2.R
import com.abra.homework_8_2.data.CarInfo
import com.abra.homework_8_2.database.CarInfoDAO
import com.abra.homework_8_2.database.DataBaseCarInfo
import com.abra.homework_8_2.functions.createDirectory
import com.abra.homework_8_2.functions.saveImage
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File

private const val REQUEST_CODE_PHOTO = 1

class FragmentAddCar : Fragment(R.layout.fragment_add_car) {
    private lateinit var loader: FragmentLoader
    private lateinit var textName: EditText
    private lateinit var textProducer: EditText
    private lateinit var textModel: EditText
    private lateinit var imgButtonBack: ImageButton
    private lateinit var imgButtonApply: ImageButton
    private lateinit var fab: FloatingActionButton
    private lateinit var carPhoto: ImageView
    private lateinit var noCarPhoto: TextView
    private var photoWasLoaded: Boolean = false
    private lateinit var carPictureDirectory: File
    private lateinit var pathToPicture: String
    private lateinit var database: DataBaseCarInfo
    private lateinit var carInfoDAO: CarInfoDAO
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(view) {
            carPhoto = findViewById(R.id.imageCarPhoto)
            textName = findViewById(R.id.etOwnerName)
            textProducer = findViewById(R.id.etProducer)
            textModel = findViewById(R.id.etModel)
            imgButtonBack = findViewById(R.id.imgButtonBack)
            imgButtonApply = findViewById(R.id.imgButtonApply)
            fab = findViewById(R.id.fabLoadPhoto)
            noCarPhoto = findViewById(R.id.tvNoPhoto)
        }
        loader = requireActivity() as FragmentLoader
        initDatabase()
        createDirectoryForPictures()
        setListeners()
    }

    private fun initDatabase() {
        database = DataBaseCarInfo.getDataBase(context as Context)
        carInfoDAO = database.getCarInfoDAO()
    }

    private fun setListeners() {
        imgButtonBack.setOnClickListener {
            backToMainFragment()
        }
        imgButtonApply.setOnClickListener {
            addCarInfo()
        }
        fab.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, REQUEST_CODE_PHOTO)
        }
    }

    private fun addCarInfo() {
        if (!photoWasLoaded) {
            pathToPicture = ""
        }
        val name = textName.text.toString()
        val producer = textProducer.text.toString()
        val model = textModel.text.toString()
        if (name.isNotEmpty() && producer.isNotEmpty() && model.isNotEmpty()) {
            carInfoDAO.add(CarInfo(pathToPicture, name, producer, model))
            backToMainFragment()
        } else {
            Toast.makeText(context, "Fields can't be empty", Toast.LENGTH_SHORT).show()
        }
    }

    private fun backToMainFragment() {
        loader.loadFragment(FragmentCarList(), FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.extras?.get("data")?.run {
            pathToPicture = saveImage(this as Bitmap, carPhoto, carPictureDirectory)
            photoWasLoaded = true
            noCarPhoto.visibility = View.INVISIBLE
        }
    }

    private fun createDirectoryForPictures() {
        createDirectory(context as Context)?.run {
            carPictureDirectory = this
        }
    }
}