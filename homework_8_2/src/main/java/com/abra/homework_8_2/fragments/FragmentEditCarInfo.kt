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
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.abra.homework_8_2.R
import com.abra.homework_8_2.data.CarInfo
import com.abra.homework_8_2.database.CarInfoDAO
import com.abra.homework_8_2.database.DataBaseCarInfo
import com.abra.homework_8_2.functions.createDirectory
import com.abra.homework_8_2.functions.saveImage
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File

private const val REQUEST_CODE_PHOTO = 1

class FragmentEditCarInfo : Fragment(R.layout.fragment_edit_car_info) {
    private var carId: Long = 0
    private lateinit var textName: EditText
    private lateinit var textProducer: EditText
    private lateinit var textModel: EditText
    private lateinit var imgButtonBack: ImageButton
    private lateinit var imgButtonApply: ImageButton
    private lateinit var fab: FloatingActionButton
    private lateinit var carPhoto: ImageView
    private var photoWasLoaded: Boolean = false
    private lateinit var pathToPicture: String
    private lateinit var carPictureDirectory: File
    private lateinit var currentCarInfo: CarInfo
    private lateinit var database: DataBaseCarInfo
    private lateinit var carInfoDAO: CarInfoDAO
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(view) {
            carPhoto = findViewById(R.id.imageCarPhoto1)
            textName = findViewById(R.id.etOwnerName1)
            textProducer = findViewById(R.id.etProducer1)
            textModel = findViewById(R.id.etModel1)
            imgButtonBack = findViewById(R.id.imgButtonBack1)
            imgButtonApply = findViewById(R.id.imgButtonApply1)
            fab = findViewById(R.id.fabLoadPhoto1)
        }
        initDatabase()
        createDirectoryForPictures()
        setListeners()
        loadDataFromIntent(requireArguments())
    }

    private fun initDatabase() {
        database = DataBaseCarInfo.getDataBase(context as Context)
        carInfoDAO = database.getCarInfoDAO()
    }

    private fun loadDataFromIntent(bundle: Bundle) {
        val carInfo = bundle.getParcelable<CarInfo>("carInfo")
        if (carInfo != null) {
            with(carInfo) {
                currentCarInfo = this
                carId = id
                val path = pathToPicture
                val file = File(path)
                if (file.exists()) {
                    if (path == "") {
                        carPhoto.setImageResource(R.drawable.default_icon)
                    } else {
                        Glide.with(context).load(path).into(carPhoto)
                        photoWasLoaded = true
                        this@FragmentEditCarInfo.pathToPicture = path
                    }
                }
                textName.setText(name)
                textProducer.setText(producer)
                textModel.setText(model)
            }
        }
    }

    private fun setListeners() {
        imgButtonBack.setOnClickListener {
            backToMainFragment()
        }
        imgButtonApply.setOnClickListener {
            editCarInfo()
        }
        fab.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, REQUEST_CODE_PHOTO)
        }
    }

    private fun editCarInfo() {
        val name = textName.text.toString()
        val producer = textProducer.text.toString()
        val model = textModel.text.toString()
        if (name.isNotEmpty() && producer.isNotEmpty() && model.isNotEmpty()) {
            if (!photoWasLoaded) {
                pathToPicture = ""
            }
            val carInfo = CarInfo(pathToPicture, name, producer, model).also { it.id = carId }
            carInfoDAO.update(carInfo)
            backToMainFragment()
        } else {
            Toast.makeText(context, "Fields can't be empty", Toast.LENGTH_SHORT).show()
        }
    }

    private fun backToMainFragment() {
        requireActivity().supportFragmentManager.commit {
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            replace<FragmentCarList>(R.id.fragmentContainer)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.extras?.get("data")?.run {
            pathToPicture = saveImage(this as Bitmap, carPhoto, carPictureDirectory)
            photoWasLoaded = true
        }
    }

    private fun createDirectoryForPictures() {
        createDirectory(context as Context)?.run {
            carPictureDirectory = this
        }
    }
}