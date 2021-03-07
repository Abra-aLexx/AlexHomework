package com.abra.homework_9.repositories

import android.content.Context

class SharedPrefRepository(val context: Context?) {
    private val preferences = context?.getSharedPreferences("lastCityChecked", Context.MODE_PRIVATE)
    fun writeId(checkedCityId: Int) {
        preferences?.edit()?.run {
            putInt("id", checkedCityId)
            apply()
        }
    }

    fun getId() = preferences?.getInt("id", 0) as Int
}