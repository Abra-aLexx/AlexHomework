package com.abra.homework_9.observer

import com.abra.homework_9.database.CityData

interface Observer {
    fun update(list: List<CityData>)
}