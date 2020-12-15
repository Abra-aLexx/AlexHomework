package com.abra.homework_four_dot_two

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), ColoredCircle.OnClickShowTouchCoordinatesListener{
    private var flag = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val coloredCircle = findViewById<ColoredCircle>(R.id.coloredCircle)
        val switch = findViewById<SwitchCompat>(R.id.switch1)
        coloredCircle.setOnClickShowTouchCoordinatesListener(this)
        switch.setOnCheckedChangeListener { view, isCheked ->
            flag = isCheked
        }
    }

    override fun onClickShowTouchCoordinates(x: Int, y: Int, view: View, color: Int) {
        if(flag){
            Snackbar.make(view,"Нажаты координаты $x, $y",Snackbar.LENGTH_SHORT).setTextColor(color).show()
        }else {
            Toast.makeText(this, "Нажаты координаты $x, $y", Toast.LENGTH_SHORT).show()
        }
    }
}