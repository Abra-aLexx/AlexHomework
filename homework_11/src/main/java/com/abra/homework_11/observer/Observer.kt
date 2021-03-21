package com.abra.homework_11.observer

import com.abra.homework_11.json_structure.LogData

interface Observer {
    fun update(list: MutableList<LogData>)
}