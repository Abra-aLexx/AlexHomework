package com.abra.homework_four_dot_two

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var switch: SwitchCompat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val coloredCircle = findViewById<ColoredCircle>(R.id.coloredCircle)
        switch = findViewById(R.id.switch1)
        coloredCircle.showCoordinatesListener = { x: Int, y: Int, view: View, color: Int ->
            if (isSnackbarEnable()) {
                Snackbar.make(view, "Нажаты координаты $x, $y", Snackbar.LENGTH_SHORT).setTextColor(color).show()
            } else {
                Toast.makeText(this, "Нажаты координаты $x, $y", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isSnackbarEnable() = switch.isChecked
}