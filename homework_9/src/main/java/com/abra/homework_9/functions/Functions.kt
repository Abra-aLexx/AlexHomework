package com.abra.homework_9.functions

import android.widget.TextView
import com.abra.homework_9.R
import java.text.SimpleDateFormat
import java.util.Date

fun setCurrentDate(tv: TextView) {
    tv.text = SimpleDateFormat.getDateInstance().format(Date())
}

fun getIconId(txtId: String): Int {
    when (txtId) {
        "01d" -> return R.drawable.clear_sky
        "02d" -> return R.drawable.few_clouds
        "03d" -> return R.drawable.scattered_clouds
        "04d" -> return R.drawable.broken_clouds
        "09d" -> return R.drawable.shower_rain
        "10d" -> return R.drawable.rain
        "11d" -> return R.drawable.thunderstorm
        "13d" -> return R.drawable.snow
        "50d" -> return R.drawable.mist

        "01n" -> return R.drawable.clear_sky_n
        "02n" -> return R.drawable.few_clouds_n
        "03n" -> return R.drawable.scattered_clouds_n
        "04n" -> return R.drawable.broken_clouds_n
        "09n" -> return R.drawable.shower_rain_n
        "10n" -> return R.drawable.rain_n
        "11n" -> return R.drawable.thunderstorm_n
        "13n" -> return R.drawable.snow_n
        "50n" -> return R.drawable.mist_n
    }
    return 0
}