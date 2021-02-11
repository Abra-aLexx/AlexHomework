package com.abra.homework_9.database

import android.app.Application
import com.abra.homework_9.repositories.DatabaseRepository

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        DatabaseRepository.initDatabase(this)
    }
}