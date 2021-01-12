package com.abra.homework_6_2

import android.app.Activity
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class WholeImageActivity : AppCompatActivity() {
    private lateinit var wholeImage: ImageView
    private lateinit var btBack: ImageButton
    private lateinit var imageName: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_whole_image)
        wholeImage = findViewById(R.id.ivWholeImage)
        btBack = findViewById(R.id.buttonBack)
        imageName = findViewById(R.id.imageName)
        loadImage()
        setButtonListener()
    }
    private fun getPathFromIntent(): Uri?{
        val path = intent.getParcelableExtra<Uri>("path")
        if (path!=null){
            return path
        }
        return null
    }
    private fun loadImage(){
        val path = getPathFromIntent()
        if (path!=null){
            Glide.with(this).load(path).into(wholeImage)
            imageName.text = path.lastPathSegment
        }
    }
    private fun setButtonListener(){
        btBack.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}