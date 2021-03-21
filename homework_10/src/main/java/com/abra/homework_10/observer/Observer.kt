package com.abra.homework_10.observer

import com.abra.homework_10.presentation.database.CityData

interface Observer {
    fun update(list: List<CityData>)
}